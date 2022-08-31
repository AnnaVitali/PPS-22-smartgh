package it.unibo.pps.smartgh.mvc.component.areaParameter

import it.unibo.pps.smartgh.controller.component.areaParameter.AreaAirHumidityControllerModule.AreaAirHumidityController
import it.unibo.pps.smartgh.controller.component.areaParameter.AreaParameterController.AreaParameterController
import it.unibo.pps.smartgh.controller.component.areaParameter.{
  AreaAirHumidityControllerModule,
  AreaParameterController
}
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameter.AreaAirHumidityViewModule.AreaAirHumidityView
import it.unibo.pps.smartgh.view.component.areaParameter.AreaParameterView.AreaParameterView
import it.unibo.pps.smartgh.view.component.areaParameter.{AreaAirHumidityViewModule, AreaParameterView}

/** Object that incapsulate the model view and controller module for area air humidity. */
object AreaAirHumidityMVC:

  /** Create a new [[AreaAirHumidityMVCImpl]].
    * @param areaModel
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    * @return
    *   a new instance of [[AreaAirHumidityMVCImpl]].
    */
  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaAirHumidityMVCImpl =
    AreaAirHumidityMVCImpl(areaModel, updateStateMessage)

  /** Implementation of the area air humidity MVC.
    * @param areaModel
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    */
  class AreaAirHumidityMVCImpl(
      override val areaModel: AreaModel,
      private val updateStateMessage: (String, Boolean) => Unit
  ) extends AreaModelModule.Interface
      with AreaAirHumidityViewModule.Interface
      with AreaAirHumidityControllerModule.Interface
      with AreaParameterMVC:

    override val parameterController: AreaParameterController = AreaAirHumidityControllerImpl(updateStateMessage)
    override val parameterView: AreaParameterView = AreaAirHumidityViewImpl()

    parameterController.initialize()
