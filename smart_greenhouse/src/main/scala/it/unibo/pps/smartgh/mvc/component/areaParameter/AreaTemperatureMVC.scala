package it.unibo.pps.smartgh.mvc.component.areaParameter

import it.unibo.pps.smartgh.controller.component.areaParameter.AreaParameterController.AreaParameterController
import it.unibo.pps.smartgh.controller.component.areaParameter.AreaTemperatureControllerModule.AreaTemperatureController
import it.unibo.pps.smartgh.controller.component.areaParameter.{
  AreaParameterController,
  AreaTemperatureControllerModule
}
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameter.AreaParameterView.AreaParameterView
import it.unibo.pps.smartgh.view.component.areaParameter.AreaTemperatureViewModule.AreaTemperatureView
import it.unibo.pps.smartgh.view.component.areaParameter.{AreaParameterView, AreaTemperatureViewModule}

/** A trait that represents the MVC component for the area temperature section. */
trait AreaTemperatureMVC extends AreaModelModule.Interface
  with AreaTemperatureViewModule.Interface
  with AreaTemperatureControllerModule.Interface
  with AreaParameterMVC

/** Object that incapsulate the model view and controller module for area temperature. */
object AreaTemperatureMVC:

  /** Create a new [[AreaTemperatureMVCImpl]].
    * @param areaModel
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    * @return
    *   a new instance of [[AreaTemperatureMVC]].
    */
  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaTemperatureMVC =
    AreaTemperatureMVCImpl(areaModel, updateStateMessage)

  private class AreaTemperatureMVCImpl(override val areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit)
    extends AreaTemperatureMVC:

    override val parameterController: AreaParameterController = AreaTemperatureControllerImpl(updateStateMessage)
    override val parameterView: AreaParameterView = AreaTemperatureViewImpl()

    parameterController.initialize()
