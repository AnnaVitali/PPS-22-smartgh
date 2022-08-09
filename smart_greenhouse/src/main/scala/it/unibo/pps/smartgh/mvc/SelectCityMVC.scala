package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.SelectCityControllerModule
import it.unibo.pps.smartgh.controller.SelectCityControllerModule.SelectCityController
import it.unibo.pps.smartgh.model.city.{SelectCityModelModule, UploadCities}
import it.unibo.pps.smartgh.model.city.SelectCityModelModule.SelectCityModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.{BaseView, SelectCityViewModule}
import it.unibo.pps.smartgh.view.component.SelectCityViewModule.SelectCityView

/** Object that incapsulate the model view and controller module for the plant selection. */
object SelectCityMVC:

  val fileName: String = System.getProperty("user.home") + "/pps/cities.pl"

  def apply(simulationMVC: SimulationMVCImpl, baseView: BaseView): SelectCityMVCImpl =
    SelectCityMVCImpl(simulationMVC, baseView)

  class SelectCityMVCImpl(simulationMVC: SimulationMVCImpl, baseView: BaseView)
      extends SelectCityModelModule.Interface
      with SelectCityViewModule.Interface
      with SelectCityControllerModule.Interface:
    
    override val selectCityModel: SelectCityModel = SelectCityModelImpl(fileName)
    override val selectCityView: SelectCityView = SelectCityViewViewImpl(simulationMVC.simulationView, baseView)
    override val selectCityController: SelectCityController = SelectCityControllerImpl(simulationMVC)