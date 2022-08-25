package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.city.SelectCityModelModule
import it.unibo.pps.smartgh.model.greenhouse.Environment
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.component.SelectCityViewModule

/** Object that encloses the controller module for the city selection. */
object SelectCityControllerModule:

  /** A trait that represents the controller for the scene of city selection. */
  trait SelectCityController:

    /** Save the selected city and change.
      * @param name
      *   name of the selected city.
      */
    def saveCity(name: String): Unit

    /** Retrieves all cities.
      * @return
      *   a sequences of city names.
      */
    def getAllCities: Seq[String]

    /** Method for searching cities beginning with the given characters.
      * @param charSequence
      *   a sequence of characters that the name of the city begins with.
      * @return
      *   a sequence of city names.
      */
    def searchCities(charSequence: Seq[Char]): Seq[String]

    /** Check whether all cities contain a given city.
      * @param city
      *   the city to test.
      * @return
      *   true if contain the city, false otherwise.
      */
    def containCity(city: String): Boolean

  /** Trait that represents the provider of the controller for the city selection. */
  trait Provider:

    /** The controller of city selection. */
    val selectCityController: SelectCityController

  /** The controller requirements. */
  type Requirements = SelectCityViewModule.Provider with SelectCityModelModule.Provider with SimulationMVC.Provider

  /** Trait that represent the controller component for the city selection. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[SelectCityController]] implementation. */
    class SelectCityControllerImpl() extends SelectCityController:

      override def saveCity(name: String): Unit = simulationMVC.simulationController.environment = Environment(name)
      override def getAllCities: Seq[String] = selectCityModel.getAllCities
      override def searchCities(charSequence: Seq[Char]): Seq[String] = selectCityModel.searchCities(charSequence)
      override def containCity(city: String): Boolean = selectCityModel.containCity(city)

  /** Trait that combine provider and component for city selection. */
  trait Interface extends Provider with Component:
    self: Requirements =>
