package plant

import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, TermVisitor, Theory, Var}

import java.util

trait PlantSelector:
  def getAllAvailablePlants: List[String]
  def selectPlant(plantName: String): Unit
  def getPlantsSelectedName: List[String]
  def getPlantsSelectedIdentifier: List[String]

object PlantSelector:
  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")

  def apply(): PlantSelector = PlantSelectorImpl()

  private class PlantSelectorImpl extends PlantSelector:
    private var selectedPlants: List[String] = List()

    private def extractTerm(t: Term, i: Int): Term =
      t.asInstanceOf[Struct].getArg(i).getTerm

    private def prologEngine: Term => Iterable[SolveInfo] =
      val engine = new Prolog
      engine.setTheory(Theory(getClass.getResource("/plants.pl").openStream()))

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
      (prologEngine("plant(X, Y)") map (_.getTerm("X").toString)).toList

    override def selectPlant(plantName: String): Unit = selectedPlants = selectedPlants :+ plantName

    override def getPlantsSelectedName: List[String] = selectedPlants
    override def getPlantsSelectedIdentifier: List[String] =
      selectedPlants.map(s => (prologEngine("plant(" + s + ", Y)") map (_.getTerm("Y").toString)).toList).flatten
