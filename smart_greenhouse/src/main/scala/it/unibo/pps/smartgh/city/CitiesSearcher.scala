package it.unibo.pps.smartgh.city

import alice.tuprolog.{Prolog, SolveInfo, Struct, Term, Theory}

import scala.io.Source
import scala.util.Using

/** A trait exposing methods for managing city searches */
trait CitiesSearcher:

  /** Retrieves all cities.
    * @return
    *   a sequences of city names
    */
  def getAllCities: Seq[String]

  /** Method for searching cities beginning with the given characters.
    * @param charSequence
    *   a sequence of characters that the name of the city begins with
    * @return
    *   a list of city names
    */
  def searchCities(charSequence: Seq[Char]): Seq[String]

/** Object that can used to create a new instances of [[CitiesSearcher]]. */
object CitiesSearcher:
  given Conversion[String, Term] = Term.createTerm(_)
  given Conversion[Seq[_], Term] = _.mkString("[", ",", "]")
  given Conversion[String, Theory] = Theory.parseLazilyWithStandardOperators(_)

  /** Creates a new [[CitiesSearcher]] object.
    * @return
    *   a new instance of [[CitiesSearcher]]
    */
  def apply(fileName: String): CitiesSearcher = CitiesSearcherImpl(fileName)

  private class CitiesSearcherImpl(fileName: String) extends CitiesSearcher:
    import it.unibo.pps.smartgh.prolog.Scala2P.{*, given}
    private val prologFile = Using(Source.fromFile(fileName)) {
      _.mkString
    }.get
    private val engine = prologEngine(
      Theory.parseLazilyWithStandardOperators(prologFile)
    )
    override def getAllCities: Seq[String] =
      engine("citta(X)").map(extractTermToString(_, "X").replace("'", "")).toSeq

    override def searchCities(charSequence: Seq[Char]): Seq[String] =
      engine("ricerca_citta(" + charSequence.mkString("['", "','", "'|_]") + ", X)")
        .map(extractTermToString(_, "X").replace("'", ""))
        .toSeq
