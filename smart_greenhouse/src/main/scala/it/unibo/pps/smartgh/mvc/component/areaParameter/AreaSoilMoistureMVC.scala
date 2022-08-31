package it.unibo.pps.smartgh.mvc.component.areaParameter

import it.unibo.pps.smartgh.controller.component.areaParameter.AreaParameterController.AreaParameterController
import it.unibo.pps.smartgh.controller.component.areaParameter.AreaSoilMoistureControllerModule.AreaSoilMoistureController
import it.unibo.pps.smartgh.controller.component.areaParameter.{
  AreaParameterController,
  AreaSoilMoistureControllerModule
}
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameter.AreaParameterView.AreaParameterView
import it.unibo.pps.smartgh.view.component.areaParameter.AreaSoilMoistureViewModule.AreaSoilMoistureView
import it.unibo.pps.smartgh.view.component.areaParameter.{AreaParameterView, AreaSoilMoistureViewModule}

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
    * @param areaModel
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    */
  class AreaSoilMoistureMVCImpl(override val areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaSoilMoistureViewModule.Interface
      with AreaSoilMoistureControllerModule.Interface
      with AreaParameterMVC:

    override val parameterController: AreaParameterController = AreaSoilMoistureControllerImpl(updateStateMessage)
    override val parameterView: AreaParameterView = AreaSoilMoistureViewImpl()

    parameterController.initialize()
