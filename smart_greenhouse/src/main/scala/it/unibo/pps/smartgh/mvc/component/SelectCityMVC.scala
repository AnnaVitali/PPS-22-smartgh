package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.Config
import it.unibo.pps.smartgh.controller.component.SelectCityControllerModule
import it.unibo.pps.smartgh.controller.component.SelectCityControllerModule.SelectCityController
import it.unibo.pps.smartgh.model.city.SelectCityModelModule
import it.unibo.pps.smartgh.model.city.SelectCityModelModule.SelectCityModel
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.SelectCityViewModule.SelectCityView
import it.unibo.pps.smartgh.view.component.SelectCityViewModule

/** Object that encapsulates the model view and controller module for the plant selection. */
object SelectCityMVC:

  /** Create a new [[SelectCityMVCImpl]].
    * @param simulationMVC
    *   the [[SimulationMVCImpl]] of the application.
    * @return
    *   a new instance of [[SelectCityMVCImpl]].
    */
  def apply(simulationMVC: SimulationMVCImpl): SelectCityMVCImpl = SelectCityMVCImpl(simulationMVC)

  /** Implementation of the select city MVC.
    * @param simulationMVC
    *   the current [[SimulationMVCImpl]] of the application.
    */
  class SelectCityMVCImpl(override val simulationMVC: SimulationMVCImpl)
      extends SelectCityModelModule.Interface
      with SelectCityViewModule.Interface
      with SelectCityControllerModule.Interface
      with SimulationMVC.Interface:

    override val selectCityModel: SelectCityModel = SelectCityModelImpl(Config.Path + Config.CitiesOutputFile)
    override val selectCityView: SelectCityView = SelectCityViewViewImpl()
    override val selectCityController: SelectCityController = SelectCityControllerImpl()
