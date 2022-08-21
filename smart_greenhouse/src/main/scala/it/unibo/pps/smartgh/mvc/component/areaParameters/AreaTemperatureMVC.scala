package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.AreaParametersController
import it.unibo.pps.smartgh.controller.component.areaParameters.{
  AreaParametersController,
  AreaTemperatureControllerModule
}
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaTemperatureControllerModule.AreaTemperatureController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.AreaParametersView
import it.unibo.pps.smartgh.view.component.areaParameters.{AreaParametersView, AreaTemperatureViewModule}
import it.unibo.pps.smartgh.view.component.areaParameters.AreaTemperatureViewModule.AreaTemperatureView

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
    * @param model
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    */
  class AreaTemperatureMVCImpl(model: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaTemperatureViewModule.Interface
      with AreaTemperatureControllerModule.Interface
      with AreaParametersMVC:

    override val areaModel: AreaModel = model
    override val parameterController: AreaParametersController = AreaTemperatureControllerImpl(updateStateMessage)
    override val parameterView: AreaParametersView = AreaTemperatureViewImpl()

    parameterController.initializeView(parameterView)
