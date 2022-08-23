package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.Config
import it.unibo.pps.smartgh.controller.component.SelectCityControllerModule
import it.unibo.pps.smartgh.controller.component.SelectCityControllerModule.SelectCityController
import it.unibo.pps.smartgh.model.city.SelectCityModelModule
import it.unibo.pps.smartgh.model.city.SelectCityModelModule.SelectCityModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.SelectCityViewModule.SelectCityView
import it.unibo.pps.smartgh.view.component.{BaseView, SelectCityViewModule}

/** Object that incapsulates the model view and controller module for the plant selection. */
object SelectCityMVC:

  /** Create a new [[SelectCityMVCImpl]].
    * @param simulationMVC
    *   the [[SimulationMVCImpl]] of the application.
    * @param baseView
    *   the view in which the [[SelectCityView]] is enclosed.
    * @return
    *   a new instance of [[SelectCityMVCImpl]].
    */
  def apply(simulationMVC: SimulationMVCImpl, baseView: BaseView): SelectCityMVCImpl =
    SelectCityMVCImpl(simulationMVC, baseView)

  /** Implementation of the select city MVC.
    * @param simulationMVC
    *   the [[SimulationMVCImpl]] of the application.
    * @param baseView
    *   the view in which the [[SelectCityView]] is enclosed.
    */
  class SelectCityMVCImpl(simulationMVC: SimulationMVCImpl, baseView: BaseView)
      extends SelectCityModelModule.Interface
      with SelectCityViewModule.Interface
      with SelectCityControllerModule.Interface:

    override val selectCityModel: SelectCityModel = SelectCityModelImpl(Config.path + Config.citiesOutputFile)
    override val selectCityView: SelectCityView = SelectCityViewViewImpl(simulationMVC.simulationView, baseView)
    override val selectCityController: SelectCityController = SelectCityControllerImpl(simulationMVC)
