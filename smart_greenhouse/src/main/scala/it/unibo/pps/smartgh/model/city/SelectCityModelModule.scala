package it.unibo.pps.smartgh.model.city

import alice.tuprolog.*

import scala.io.Source
import scala.util.Using

/** Object that encloses the model module for the city selection. */
object SelectCityModelModule:

  /** A trait exposing methods for managing city searches. */
  trait SelectCityModel:

    /** Method for searching cities beginning with the given name.
      * @param name
      *   the name of the city to search.
      * @return
      *   a sequence of city names.
      */
    def searchCities(name: String): Seq[String]

    /** Retrive the city's latitude and longitude information.
      * @param city
      *   the city to find
      * @return
      *   an [[Option]] containing the (city, latitude, longitude) values or none
      */
    def getCityInfo(city: String): Option[(String, String, String)]

  /** Trait that represents the provider of the model for the city selection. */
  trait Provider:
    /** The model of selectCity. */
    val selectCityModel: SelectCityModel

  /** Trait that represents the components of the model for the city selection. */
  trait Component:

    /** Class that contains the [[SelectCityModel]] implementation.
      * @param citiesFilePath
      *   the cities file path.
      */
    class SelectCityModelImpl(private val citiesFilePath: String) extends SelectCityModel:
      import it.unibo.pps.smartgh.prolog.Scala2P.{prologEngine, extractTermToString, given}
      private val prologFile = Using(Source.fromFile(citiesFilePath, enc = "UTF8"))(_.mkString).getOrElse("")
      private val engine = prologEngine(Theory.parseLazilyWithStandardOperators(prologFile))

      override def searchCities(name: String): Seq[String] =
        searchCity(name, end = "'|_]").map(extractTerm(_)("X")).toSeq

      override def getCityInfo(city: String): Option[(String, String, String)] =
        searchCity(city).headOption.fold(None) { s =>
          val e = extractTerm(s)
          Some(e("X"), e("Y"), e("Z"))
        }

      private def searchCity(city: String, start: String = "['", sep: String = "','", end: String = "']") =
        engine("search_city(" + city.mkString(start, sep, end) + ", X, Y, Z)")

      private def extractTerm(solveInfo: SolveInfo)(term: String) =
        extractTermToString(solveInfo, term).replace("'", "")

  /** Trait that encloses the model for the city selection. */
  trait Interface extends Provider with Component
