package it.unibo.pps.smartgh.model.plants

import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, TermVisitor, Theory, Var}
import it.unibo.pps.smartgh.model.plants.Plant
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import monix.execution.{Ack, Cancelable}
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.{MulticastStrategy, Observable}
import org.scalactic.TripleEquals.convertToEqualizer

import java.util
import scala.concurrent.Future
import scala.io.Source
import scala.language.postfixOps
import scala.util.Using

/** Object that encloses the model module for the plant selection. */
object PlantSelectorModelModule:

  /** This trait exposes methods for managing the selection of the plants. */
  trait PlantSelectorModel:

    /** Method for obtaining all the available plants that can be cultivated in the greenhouse.
      * @return
      *   the [[List]] of the name of all the plants available.
      */
    def getAllAvailablePlants: List[String]

    /** Method that registers the callback for the plant [[Observable]].
      * @param onNext
      *   specify which is the action that needs to be taken when a new plant has been selected.
      * @param onError
      *   specify which is the action that needs to be taken when an error occur.
      * @param onComplete
      *   specify which is the action that needs to be taken when the emission of the data ends.
      */
    def registerCallbackPlantSelection(
        onNext: List[String] => Future[Ack],
        onError: Throwable => Unit,
        onComplete: () => Unit
    ): Cancelable

    /** Method that registers the callback for the plant information.
      *
      * @param onNext
      *   specify which is the action that needs to be taken when new plant information are available.
      * @param onError
      *   specify which is the action that needs to be taken when an error occur.
      * @param onComplete
      *   specify which is the action that needs to be taken when the emission of the data ends.
      */
    def registerCallbackPlantInfo(
        onNext: Plant => Future[Ack],
        onError: Throwable => Unit,
        onComplete: () => Unit
    ): Cancelable

    /** Method that need to be called to select a plants that you want to cultivate.
      * @param plantName
      *   the name of the selected plant.
      */
    def selectPlant(plantName: String): Unit

    /** Method that need to be called to deselect a plants that you don't want to cultivate.
      * @param plantName
      *   the name of the plant to deselect.
      * @throws NoSuchElementException
      *   if the specified plant has not been previously selected.
      */
    def deselectPlant(plantName: String): Unit

    /** Method that returns the name of the plants selected for the cultivation in the greenhouse.
      * @return
      *   the [[List]] of the name of the plants selected.
      */
    def getPlantsSelectedName: List[String]

    /** Method that returns the identifier of the plants selected for the cultivation in the greenhouse.
      * @return
      *   the [[List]] of the identifier of the plants selected.
      */
    def getPlantsSelectedIdentifier: List[String]

    /** Method that returns the number of plants selected.
      * @return
      *   the selected plants count.
      */
    def getPlantSelectedCount: Int

    /** Method that returns the plants selected for the cultivation in the greenhouse. */
    def startEmittingPlantsSelected(): Unit

  /** Trait that represents the provider of the model for the plant selection. */
  trait Provider:
    /** The plant selector model. */
    val plantSelectorModel: PlantSelectorModel

  /** Trait that represents the components of the model for the plant selection. */
  trait Component:
    /** Class that contains the [[PlantSelectorModel]] implementation.
      * @param fileName
      *   the name of the prolog file from which the plants will be taken.
      */
    class PlantSelectorModelImpl(fileName: String) extends PlantSelectorModel:
      import it.unibo.pps.smartgh.prolog.Scala2P.{extractTermToString, prologEngine, given}
      private val prologFile = Using(Source.fromFile(fileName, enc = "UTF8")) {
        _.mkString
      }.getOrElse("")
      private val engine = prologEngine(
        Theory.parseLazilyWithStandardOperators(prologFile)
      )
      private var selectedPlants: Vector[String] = Vector.empty
      private val subjectPlantSelection: ConcurrentSubject[List[String], List[String]] =
        ConcurrentSubject[List[String]](MulticastStrategy.publish)
      private val subjectPlantInfo: ConcurrentSubject[Plant, Plant] =
        ConcurrentSubject[Plant](MulticastStrategy.publish)

      override def getAllAvailablePlants: List[String] =
        engine("plant(X, Y).").map(extractTermToString(_, "X").replace("'", "")).toList

      override def registerCallbackPlantSelection(
          onNext: List[String] => Future[Ack],
          onError: Throwable => Unit,
          onComplete: () => Unit
      ): Cancelable =
        subjectPlantSelection.subscribe(onNext, onError, onComplete)

      override def registerCallbackPlantInfo(
          onNext: Plant => Future[Ack],
          onError: Throwable => Unit,
          onComplete: () => Unit
      ): Cancelable =
        subjectPlantInfo.subscribe(onNext, onError, onComplete)

      override def selectPlant(plantName: String): Unit =
        Task {
          selectedPlants = selectedPlants :+ plantName
          subjectPlantSelection.onNext(selectedPlants.toList)
        }.executeAsync.runToFuture

      override def deselectPlant(plantName: String): Unit =
        Task {
          if selectedPlants.contains(plantName) then
            selectedPlants = selectedPlants.filter(_ !== plantName)
            subjectPlantSelection
              .onNext(selectedPlants.toList)
          else
            subjectPlantSelection.onError(
              new NoSuchElementException("You can't deselect a plant that hasn't been selected!")
            )
        }.executeAsync.runToFuture

      override def getPlantsSelectedName: List[String] = selectedPlants.toList

      override def getPlantsSelectedIdentifier: List[String] =
        selectedPlants
          .map(s => "\'" + s + "\'")
          .flatMap(s => engine("plant(" + s + ", Y).").map(extractTermToString(_, "Y")))
          .toList

      override def startEmittingPlantsSelected(): Unit =
        Task {
          selectedPlants
            .zip(getPlantsSelectedIdentifier)
            .foreach((name, identifier) => subjectPlantInfo.onNext(Plant(name, identifier)))
          subjectPlantInfo.onComplete()
        }.executeAsync.runToFuture

      override def getPlantSelectedCount: Int = selectedPlants.size

  /** Trait that encloses the model for the plant selection. */
  trait Interface extends Provider with Component
