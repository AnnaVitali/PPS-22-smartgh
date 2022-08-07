package it.unibo.pps.smartgh.model.plants

import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, TermVisitor, Theory, Var}

import java.util
import scala.io.Source
import scala.util.Using
import monix.eval.Task
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.Observable
import monix.reactive.MulticastStrategy
import monix.execution.Scheduler.Implicits.global
import monix.execution.Ack
import concurrent.{Promise, Future}

/** Object that encloses the model module for the plant selection. */
object PlantSelectorModelModule:

  /** This trait exposes methods for managing the selection of plants. */
  trait PlantSelectorModel:

    /** Method for obtaining all the available plants that can be cultivated in the greenhouse.
      * @return
      *   the [[List]] of the name of all the plants available.
      */
    def getAllAvailablePlants(): List[String]

    /** Method that can be called to obtain the [[Observable]] associated to the selected plant.
      * @return
      *   the [[Observable]] associated to the selected plant.
      */
    def registerCallback(onNext: List[String] => Future[Ack], onError: Throwable => Unit, onComplete: () => Unit): Unit

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

    /** Method that returns the name of the plants selected for cultivation in the greenhouse.
      * @return
      *   the [[List]] of the neme of the plants selected.
      */
    def getPlantsSelectedName(): List[String]

    /** Method that returns the identifier of the plants selected for cultivation in the greenhouse.
      * @return
      *   the [[List]] of the identifier of the plants selected.
      */
    def getPlantsSelectedIdentifier(): List[String]

  /** Trait that represents the provider of the model for the plant selection. */
  trait Provider:
    val plantSelectorModel: PlantSelectorModel

  /** Trait that represents the components of the model for the plant selection. */
  trait Component:
    /** Class that contains the [[PlantSelectorModel]] implementation.
      * @param fileName
      */
    class PlantSelectorModelImpl(fileName: String) extends PlantSelectorModel:
      import it.unibo.pps.smartgh.prolog.Scala2P.{*, given}
      private val prologFile = Using(Source.fromFile(fileName, enc = "UTF8")) {
        _.mkString
      }.getOrElse("")
      private val engine = prologEngine(
        Theory.parseLazilyWithStandardOperators(prologFile)
      )
      private var selectedPlants: List[String] = List()
      private val subject: ConcurrentSubject[List[String], List[String]] =
        ConcurrentSubject[List[String]](MulticastStrategy.publish)

      override def getAllAvailablePlants(): List[String] =
        engine("plant(X, Y).").map(extractTermToString(_, "X").replace("'", "")).toList

      override def registerCallback(
          onNext: List[String] => Future[Ack],
          onError: Throwable => Unit,
          onComplete: () => Unit
      ): Unit =
        subject.subscribe(onNext, onError, onComplete)

      override def selectPlant(plantName: String): Unit =
        selectedPlants = selectedPlants :+ plantName
        subject.onNext(selectedPlants)

      override def deselectPlant(plantName: String): Unit =
        if selectedPlants.contains(plantName) then
          selectedPlants = selectedPlants.take(selectedPlants.indexOf(plantName))
          subject.onNext(selectedPlants)
        else throw new NoSuchElementException("You can't deselect a plant that hasn't been selected!")

      override def getPlantsSelectedName(): List[String] = selectedPlants

      override def getPlantsSelectedIdentifier(): List[String] =
        selectedPlants.flatMap(s => engine("plant(" + s + ", Y)").map(extractTermToString(_, "Y")))

  /** Trait that encloses the model for the plant selection. */
  trait Interface extends Provider with Component
