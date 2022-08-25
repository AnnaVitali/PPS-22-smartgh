package it.unibo.pps.smartgh.prolog

import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, Theory}

/** This object exposed methods for the integration of scale and prolog. */
object Scala2P:
  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")
  given Conversion[String, Theory] = Theory.parseLazilyWithStandardOperators(_)

  /** Extracting a term from the given term by index.
    * @param t
    *   the term to extract
    * @param i
    *   the index of the term
    * @return
    *   the extracted term
    */
  def extractTerm(t: Term, i: Int): Term = t.asInstanceOf[Struct].getArg(i).getTerm

  /** Extract a term and convert to a string.
    * @param solveInfo
    *   the solve info
    * @param s
    *   the term to extract
    * @return
    *   the extracted term
    */
  def extractTermToString(solveInfo: SolveInfo, s: String): String = solveInfo.getTerm(s).toString

  /** The prolog engine for solving goals from the given theory.
    * @param theory
    *   the theory used for solving goal
    * @return
    *   a [[Iterable]] of [[SolveInfo]]
    */
  def prologEngine(theory: Theory): Term => Iterable[SolveInfo] =
    val engine = new Prolog
    engine.setTheory(theory)

    goal =>
      new Iterable[SolveInfo]:

        override def iterator: Iterator[SolveInfo] = new Iterator[SolveInfo]:
          var solution: Option[SolveInfo] = Some(engine.solve(goal))

          override def hasNext: Boolean =
            solution.fold(false)(f => f.hasOpenAlternatives || f.isSuccess)

          @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
          override def next(): SolveInfo =
            try solution.get
            finally solution = if (solution.get.hasOpenAlternatives) Some(engine.solveNext()) else None
