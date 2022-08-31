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

/** Object that incapsulate the model view and controller module for area temperature. */
object AreaTemperatureMVC:

  /** Create a new [[AreaTemperatureMVCImpl]].
    * @param areaModel
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    * @return
    *   a new instance of [[AreaTemperatureMVCImpl]].
    */
  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaTemperatureMVCImpl =
    AreaTemperatureMVCImpl(areaModel, updateStateMessage)

  /** Implementation of the area temperature MVC.
    * @param areaModel
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    */
  class AreaTemperatureMVCImpl(override val areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaTemperatureViewModule.Interface
      with AreaTemperatureControllerModule.Interface
      with AreaParameterMVC:

    override val parameterController: AreaParameterController = AreaTemperatureControllerImpl(updateStateMessage)
    override val parameterView: AreaParameterView = AreaTemperatureViewImpl()

    parameterController.initialize()
