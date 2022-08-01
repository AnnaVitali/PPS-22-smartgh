package it.unibo.pps.smartgh.prolog

import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, Theory}

object Scala2P:
  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")
  given Conversion[String, Theory] = Theory.parseLazilyWithStandardOperators(_)

  def extractTerm(t: Term, i: Int): Term =
    t.asInstanceOf[Struct].getArg(i).getTerm

  def extractTermToString(solveInfo: SolveInfo, s: String): String =
    solveInfo.getTerm(s).toString

  def prologEngine(theory: Theory): Term => Iterable[SolveInfo] =
    val engine = new Prolog
    engine.setTheory(theory)

    goal =>
      new Iterable[SolveInfo]:

        override def iterator: Iterator[SolveInfo] = new Iterator[SolveInfo]:
          var solution: Option[SolveInfo] = Some(engine.solve(goal))

          override def hasNext: Boolean =
            solution.fold(false)(f => f.hasOpenAlternatives || f.isSuccess)

          override def next(): SolveInfo =
            try solution.get
            finally solution = if (solution.get.hasOpenAlternatives) Some(engine.solveNext()) else None
