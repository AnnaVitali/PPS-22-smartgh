package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaSoilMoistureControllerModule.AreaSoilMoistureController
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaSoilMoistureControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.areaParameters.AreaSoilMoistureViewModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaSoilMoistureViewModule.AreaSoilMoistureView

object AreaSoilMoistureMVC:

  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaSoilMoistureMVCImpl =
    AreaSoilMoistureMVCImpl(areaModel, updateStateMessage)

  class AreaSoilMoistureMVCImpl(model: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaSoilMoistureViewModule.Interface
      with AreaSoilMoistureControllerModule.Interface:

    override val areaModel: AreaModel = model
    override val areaSoilMoistureController: AreaSoilMoistureController = AreaSoilMoistureControllerImpl(
      updateStateMessage
    )
    override val areaSoilMoistureView: AreaSoilMoistureView = AreaSoilMoistureViewImpl()

    areaSoilMoistureController.initializeView()
