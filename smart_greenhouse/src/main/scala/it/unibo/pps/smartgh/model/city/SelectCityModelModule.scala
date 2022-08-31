package it.unibo.pps.smartgh.model.city

import alice.tuprolog.*

import scala.io.Source
import scala.util.Using

/** Object that encloses the model module for the city selection. */
object SelectCityModelModule:

  /** A trait exposing methods for managing city searches. */
  trait SelectCityModel:

    /** Retrieves all cities.
      * @return
      *   a sequences of city names
      */
    def getAllCities: Seq[String]

    /** Method for searching cities beginning with the given characters.
      * @param charSequence
      *   a sequence of characters that the name of the city begins with
      * @return
      *   a sequence of city names
      */
    def searchCities(charSequence: Seq[Char]): Seq[String]

    /** Check whether all cities contain a given city.
      * @param city
      *   the city to test
      * @return
      *   An option containing the sequence containing the term's values or none
      */
    def containCity(city: String): Option[(String, String, String)]

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
      private val cities = engine("city(X, _, _)").map(s => extractTerm(s, "X")).toSeq

      override def getAllCities: Seq[String] = cities

      override def searchCities(charSequence: Seq[Char]): Seq[String] =
        engine("search_city(" + charSequence.mkString("['", "','", "'|_]") + ", X, Y, Z)").map(s => extractTerm(s, "X")).toSeq

      override def containCity(city: String): Option[(String, String, String)] =
        if cities.contains(city) then
          val i = engine("search_city(" + city.mkString("['", "','", "']") + ", X, Y, Z)")
          val x = i.map(s => extractTerm(s, "X")).head
          val y = i.map(s => extractTerm(s, "Y")).head
          val z = i.map(s => extractTerm(s, "Z")).head
          Some((x,y,z))
        else
          None

      private def extractTerm(solveInfo: SolveInfo, variable: String) = extractTermToString(solveInfo, variable).replace("'", "")

  /** Trait that encloses the model for the city selection. */
  trait Interface extends Provider with Component
