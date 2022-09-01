package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.city.SelectCityModelModule
import it.unibo.pps.smartgh.model.greenhouse.Environment
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.component.SelectCityViewModule

/** Object that encloses the controller module for the city selection. */
object SelectCityControllerModule:

  /** A trait that represents the controller for the scene of city selection. */
  trait SelectCityController:

    /** Method for searching cities beginning with the given name.
      * @param name
      *   the name of the city to search.
      * @return
      *   a sequence of city names.
      */
    def searchCities(name: String): Seq[String]

    /** Check whether all cities contain a given city.
      * @param city
      *   the city to test.
      * @return
      *   true if contain the city, false otherwise.
      */
    def checkCity(city: String): Unit

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

      private def saveCity(city: String, latitude: String, longitude: String): Unit =
        simulationMVC.simulationController.environment = Environment(city, latitude, longitude)
        selectCityView.showNextScene()

      override def searchCities(name: String): Seq[String] = selectCityModel.searchCities(name)
      override def checkCity(city: String): Unit = city match
        case c if c.isBlank => selectCityView.setErrorText("Please select a city")
        case c =>
          selectCityModel
            .getCityInfo(c)
            .fold(selectCityView.setErrorText("The selected city is not valid"))(saveCity.tupled(_))

  /** Trait that combine provider and component for city selection. */
  trait Interface extends Provider with Component:
    self: Requirements =>
