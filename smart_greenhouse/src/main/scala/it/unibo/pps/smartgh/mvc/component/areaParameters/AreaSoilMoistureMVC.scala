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

/** Object that incapsulate the model view and controller module for area soil humidity. */
object AreaSoilMoistureMVC:

  /** Create a new [[AreaSoilMoistureMVCImpl]].
    * @param areaModel
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    * @return
    *   a new instance of [[AreaSoilMoistureMVCImpl]].
    */
  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaSoilMoistureMVCImpl =
    AreaSoilMoistureMVCImpl(areaModel, updateStateMessage)

  /** Implementation of the area soil moisture MVC.
    * @param model
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    */
  class AreaSoilMoistureMVCImpl(model: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaSoilMoistureViewModule.Interface
      with AreaSoilMoistureControllerModule.Interface
      with AreaParametersMVC:

    override val areaModel: AreaModel = model
    override val parameterController: AreaParametersController = AreaSoilMoistureControllerImpl(updateStateMessage)
    override val parameterView: AreaParametersView = AreaSoilMoistureViewImpl()

    parameterController.initializeView(parameterView)
