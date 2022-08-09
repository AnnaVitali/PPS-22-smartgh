package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.city.Environment
import it.unibo.pps.smartgh.model.city.SelectCityModelModule
import it.unibo.pps.smartgh.model.city.SelectCityModelModule.SelectCityModel
import it.unibo.pps.smartgh.view.component.{BaseView, SelectCityViewModule}
import it.unibo.pps.smartgh.view.component.SelectCityViewModule.SelectCityView
import it.unibo.pps.smartgh.controller.SimulationControllerModule.SimulationController
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.PlantSelectorMVC

/** Object that encloses the controller module for the city selection. */
object SelectCityControllerModule:

  /** A trait that represents the controller for the scene of city selection. */
  trait SelectCityController:

    /** Save the selected city and change.
      * @param name
      *   name of the selected city
      * @return
      *   the saved city
      */
    def saveCity(name: String): Unit

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

    //TODO add doc
    def nextMVC(baseView: BaseView): Unit

  /** Trait that represents the provider of the controller for the city selection. */
  trait Provider:
    /** The controller of city selection */
    val selectCityController: SelectCityController

  /** The controller requirements. */
  type Requirements = SelectCityViewModule.Provider with SelectCityModelModule.Provider

  /** Trait that represents the components of the controller for the city selection. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[SelectCityController]] implementation. */
    class SelectCityControllerImpl(simulationMVC: SimulationMVCImpl) extends SelectCityController:
//      var view: SelectCityView = context.selectCityView
//      val model: SelectCityModel = context.selectCityModel

      override def saveCity(name: String): Unit =
        simulationMVC.simulationController.environment = Environment(name)

      override def nextMVC(baseView: BaseView): Unit =
        val plantSelectorMVC = PlantSelectorMVC(simulationMVC, baseView)
        context.selectCityView.moveToNextScene(plantSelectorMVC.selectPlantView)

      override def getAllCities: Seq[String] = context.selectCityModel.getAllCities
      override def searchCities(charSequence: Seq[Char]): Seq[String] = context.selectCityModel.searchCities(charSequence)
      override def containCity(city: String): Boolean = context.selectCityModel.containCity(city)

  /** Trait that encloses the controller for the city selection. */
  trait Interface extends Provider with Component:
    self: Requirements =>
