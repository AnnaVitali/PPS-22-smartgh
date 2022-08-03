package it.unibo.pps.smartgh.model.plants

import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, TermVisitor, Theory, Var}

import java.util
import scala.io.Source
import scala.util.Using

object PlantSelectorModel:

  /** This trait exposes methods for managing the selection of plants. */
  trait Model:

    /** Method for obtaining all the available plants that can be cultivated in the greenhouse.
      * @return
      *   the [[List]] of the name of all the plants available.
      */
    def getAllAvailablePlants(): List[String]

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

  trait PlantSelectorProvider:
    val model: Model

  trait PlantSelectorComponent:
    class PlantSelectorModelImpl(fileName: String) extends Model:
      import it.unibo.pps.smartgh.prolog.Scala2P.{*, given}
      private val prologFile = Using(Source.fromFile(fileName, enc = "UTF8")) {
        _.mkString
      }.getOrElse("")
      private val engine = prologEngine(
        Theory.parseLazilyWithStandardOperators(prologFile)
      )
      private var selectedPlants: List[String] = List()

      override def getAllAvailablePlants(): List[String] =
        engine("plant(X, Y).").map(extractTermToString(_, "X").replace("'", "")).toList

      override def selectPlant(plantName: String): Unit =
        selectedPlants = selectedPlants :+ plantName

      override def deselectPlant(plantName: String): Unit =
        if selectedPlants.contains(plantName) then
          selectedPlants = selectedPlants.take(selectedPlants.indexOf(plantName))
        else throw new NoSuchElementException("This plant hasn't been selected")

      override def getPlantsSelectedName(): List[String] = selectedPlants

      override def getPlantsSelectedIdentifier(): List[String] =
        println(selectedPlants)
        println("plant(" + selectedPlants(0) + ", Y).")
        selectedPlants.map(s => engine("plant(" + s + ", Y)").map(extractTermToString(_, "Y"))).flatten

  trait PlantSelectorInterface extends PlantSelectorProvider with PlantSelectorComponent
