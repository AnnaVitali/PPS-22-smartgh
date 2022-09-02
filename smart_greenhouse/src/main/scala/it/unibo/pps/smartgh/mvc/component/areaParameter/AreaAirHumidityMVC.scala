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

/** A trait that represents the MVC component for the area air humidity section. */
trait AreaAirHumidityMVC extends AreaModelModule.Interface
  with AreaAirHumidityViewModule.Interface
  with AreaAirHumidityControllerModule.Interface
  with AreaParameterMVC

/** Object that incapsulate the model view and controller module for area air humidity. */
object AreaAirHumidityMVC:

  /** Create a new [[AreaAirHumidityMVCImpl]].
    * @param areaModel
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    * @return
    *   a new instance of [[AreaAirHumidityMVC]].
    */
  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaAirHumidityMVC =
    AreaAirHumidityMVCImpl(areaModel, updateStateMessage)
    
  private class AreaAirHumidityMVCImpl(
      override val areaModel: AreaModel,
      private val updateStateMessage: (String, Boolean) => Unit
  ) extends AreaAirHumidityMVC:

    override val parameterController: AreaParameterController = AreaAirHumidityControllerImpl(updateStateMessage)
    override val parameterView: AreaParameterView = AreaAirHumidityViewImpl()

    parameterController.initialize()
