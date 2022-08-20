package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.AreaParametersController
import it.unibo.pps.smartgh.controller.component.areaParameters.{
  AreaParametersController,
  AreaSoilMoistureControllerModule
}
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaSoilMoistureControllerModule.AreaSoilMoistureController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.AreaParametersView
import it.unibo.pps.smartgh.view.component.areaParameters.{AreaParametersView, AreaSoilMoistureViewModule}
import it.unibo.pps.smartgh.view.component.areaParameters.AreaSoilMoistureViewModule.AreaSoilMoistureView

object AreaSoilMoistureMVC:

  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaSoilMoistureMVCImpl =
    AreaSoilMoistureMVCImpl(areaModel, updateStateMessage)

  class AreaSoilMoistureMVCImpl(model: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaSoilMoistureViewModule.Interface
      with AreaSoilMoistureControllerModule.Interface
      with AreaParametersMVC:

    override val areaModel: AreaModel = model
    override val parameterController: AreaParametersController = AreaSoilMoistureControllerImpl(updateStateMessage)
    override val parameterView: AreaParametersView = AreaSoilMoistureViewImpl()

    parameterController.initializeView(parameterView)
