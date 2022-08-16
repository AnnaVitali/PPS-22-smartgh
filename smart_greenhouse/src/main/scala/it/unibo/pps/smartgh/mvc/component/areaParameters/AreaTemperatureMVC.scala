package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaTemperatureControllerModule
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaTemperatureControllerModule.AreaTemperatureController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.areaParameters.AreaTemperatureViewModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaTemperatureViewModule.AreaTemperatureView

object AreaTemperatureMVC:

  def apply(simulationMVC: SimulationMVCImpl, areaModel: AreaModel): AreaTemperatureMVCImpl =
    AreaTemperatureMVCImpl(simulationMVC, areaModel)

  class AreaTemperatureMVCImpl(simulationMVCImpl: SimulationMVCImpl, model: AreaModel)
      extends AreaModelModule.Interface
      with AreaTemperatureViewModule.Interface
      with AreaTemperatureControllerModule.Interface:

    override val areaModel: AreaModel = model
    override val areaTemperatureController: AreaTemperatureController = AreaTemperatureControllerImpl()
    override val areaTemperatureView: AreaTemperatureView = AreaTemperatureViewImpl()
