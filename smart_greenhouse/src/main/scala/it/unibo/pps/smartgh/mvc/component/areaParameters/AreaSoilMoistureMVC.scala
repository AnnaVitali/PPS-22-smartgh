package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaSoilMoistureControllerModule.AreaSoilMoistureController
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaSoilMoistureControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.areaParameters.AreaSoilMoistureViewModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaSoilMoistureViewModule.AreaSoilMoistureView

object AreaSoilMoistureMVC:

  def apply(simulationMVC: SimulationMVCImpl, areaModel: AreaModel): AreaSoilMoistureMVCImpl =
    AreaSoilMoistureMVCImpl(simulationMVC, areaModel)

  class AreaSoilMoistureMVCImpl(simulationMVCImpl: SimulationMVCImpl, model: AreaModel)
      extends AreaModelModule.Interface
      with AreaSoilMoistureViewModule.Interface
      with AreaSoilMoistureControllerModule.Interface:

    override val areaModel: AreaModel = model
    override val areaSoilMoistureController: AreaSoilMoistureController = AreaSoilMoistureControllerImpl()
    override val areaSoilMoistureView: AreaSoilMoistureView = AreaSoilMoistureViewImpl()
