package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.city.City
import it.unibo.pps.smartgh.model.city.CitySearcherModelModule
import it.unibo.pps.smartgh.model.city.CitySearcherModelModule.CitySearcherModel
import it.unibo.pps.smartgh.view.component.CitySearcherViewModule
import it.unibo.pps.smartgh.view.component.CitySearcherViewModule.CitySearcherView

/** Object that can be used to create a new instances of [[CityController]]. */
object CitySearcherControllerModule:

  /** This trait exposes methods for managing the selected city, represents its controller. */
  trait CitySearcherController:

    /** Save the selected city and change.
      * @param name
      *   name of the selected city
      * @return
      *   the saved city
      */
    def saveCity(name: String): City

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
      *   true if contain the city, false otherwise
      */
    def containCity(city: String): Boolean

  trait Provider:
    val citySearcherController: CitySearcherController

  type Requirements = CitySearcherViewModule.Provider with CitySearcherModelModule.Provider

  trait Component:
    context: Requirements =>
    class CitySearcherControllerImpl extends CitySearcherController:
      var view: CitySearcherView = context.citySearcherView
      val model: CitySearcherModel = context.citySearcherModel

      override def saveCity(name: String): City = City(name)
      override def getAllCities: Seq[String] = model.getAllCities
      override def searchCities(charSequence: Seq[Char]): Seq[String] = model.searchCities(charSequence)
      override def containCity(city: String): Boolean = model.containCity(city)

  trait Interface extends Provider with Component:
    self: Requirements =>
