package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaAirHumidityControllerModule.AreaAirHumidityController
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaAirHumidityControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule.AreaAirHumidityView

object AreaAirHumidityMVC:

  def apply(simulationMVC: SimulationMVCImpl, areaModel: AreaModel): AreaAirHumidityMVCImpl =
    AreaAirHumidityMVCImpl(simulationMVC, areaModel)

  class AreaAirHumidityMVCImpl(simulationMVCImpl: SimulationMVCImpl, model: AreaModel)
      extends AreaModelModule.Interface
      with AreaAirHumidityViewModule.Interface
      with AreaAirHumidityControllerModule.Interface:

    override val areaModel: AreaModel = model
    override val areaAirHumidityController: AreaAirHumidityController = AreaAirHumidityControllerImpl()
    override val areaAirHumidityView: AreaAirHumidityView = AreaAirHumidityViewImpl()
