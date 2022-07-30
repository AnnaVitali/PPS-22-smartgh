package it.unibo.pps.smartgh.plants

import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, TermVisitor, Theory, Var}

import java.util
import scala.io.Source
import scala.util.Using

/** This trait exposes methods for managing the selection of plants. */
trait PlantSelector:

  /** Method for obtaining all the available plants that can be cultivated in the greenhouse.
    * @return
    *   the [[List]] of the name of all the plants available.
    */
  def getAllAvailablePlants: List[String]

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
  def getPlantsSelectedName: List[String]

  /** Method that returns the identifier of the plants selected for cultivation in the greenhouse.
    * @return
    *   the [[List]] of the identifier of the plants selected.
    */
  def getPlantsSelectedIdentifier: List[String]

/** Object that can be use for managing the selection of plants. */
object PlantSelector:
  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")
  given Conversion[String, Theory] = Theory.parseLazilyWithStandardOperators(_)

  /** Apply method for the [[PlantSelector]].
    * @return
    *   the [[PlantSelector]] object.
    */
  def apply(fileName: String): PlantSelector = PlantSelectorImpl(fileName)

  private class PlantSelectorImpl(fileName: String) extends PlantSelector:
    private val prologFile = Using(Source.fromFile(fileName)) {
      _.mkString
    }.get
    private val engine = prologEngine(
      Theory.parseLazilyWithStandardOperators(prologFile)
    )
    private var selectedPlants: List[String] = List()

    private def extractTerm(t: Term, i: Int): Term =
      t.asInstanceOf[Struct].getArg(i).getTerm

    private def extractTermToString(solveInfo: SolveInfo, s: String): String =
      solveInfo.getTerm(s).toString

    private def prologEngine(theory: Theory): Term => Iterable[SolveInfo] =
      val engine = new Prolog
      engine.setTheory(theory)

      goal =>
        new Iterable[SolveInfo] {

          override def iterator: Iterator[SolveInfo] = new Iterator[SolveInfo] {
            var solution: Option[SolveInfo] = Some(engine.solve(goal))

            override def hasNext: Boolean =
              solution.fold(false)(f => f.hasOpenAlternatives || f.isSuccess)

            override def next(): SolveInfo =
              try solution.get
              finally solution = if (solution.get.hasOpenAlternatives) Some(engine.solveNext()) else None
          }
        }

    override def getAllAvailablePlants: List[String] =
      engine("plant(X, Y)").map(extractTermToString(_, "X")).toList

    override def selectPlant(plantName: String): Unit = selectedPlants = selectedPlants :+ plantName

    override def deselectPlant(plantName: String): Unit =
      if selectedPlants.contains(plantName) then selectedPlants = selectedPlants.take(selectedPlants.indexOf(plantName))
      else throw new NoSuchElementException("This plant hasn't been selected")

    override def getPlantsSelectedName: List[String] = selectedPlants

    override def getPlantsSelectedIdentifier: List[String] =
      selectedPlants.map(s => engine("plant(" + s + ", Y)").map(extractTermToString(_, "Y"))).flatten
