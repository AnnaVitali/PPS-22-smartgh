package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.Config
import it.unibo.pps.smartgh.controller.component.SelectCityControllerModule
import it.unibo.pps.smartgh.controller.component.SelectCityControllerModule.SelectCityController
import it.unibo.pps.smartgh.model.city.SelectCityModelModule
import it.unibo.pps.smartgh.model.city.SelectCityModelModule.SelectCityModel
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.component.SelectCityViewModule.SelectCityView
import it.unibo.pps.smartgh.view.component.SelectCityViewModule

/** A trait that represents the MVC component for select city. */
trait SelectCityMVC
    extends SelectCityModelModule.Interface
    with SelectCityViewModule.Interface
    with SelectCityControllerModule.Interface
    with SimulationMVC.Interface

/** Object that encapsulates the model view and controller module for the city selection. */
object SelectCityMVC:

  /** Create a new [[SelectCityMVCImpl]].
    * @param simulationMVC
    *   the [[SimulationMVC]] of the application.
    * @return
    *   a new instance of [[SelectCityMVC]].
    */
  def apply(simulationMVC: SimulationMVC): SelectCityMVC = SelectCityMVCImpl(simulationMVC)

  private class SelectCityMVCImpl(override val simulationMVC: SimulationMVC) extends SelectCityMVC:

    override val selectCityModel: SelectCityModel = SelectCityModelImpl(Config.Path + Config.CitiesOutputFile)
    override val selectCityView: SelectCityView = SelectCityViewViewImpl()
    override val selectCityController: SelectCityController = SelectCityControllerImpl()
