package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.{
  AreaLuminosityControllerModule,
  AreaParametersController
}
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaLuminosityControllerModule.AreaLuminosityController
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.AreaParametersController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameters.{AreaLuminosityViewModule, AreaParametersView}
import it.unibo.pps.smartgh.view.component.areaParameters.AreaLuminosityViewModule.AreaLuminosityView
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.AreaParametersView

/** Object that incapsulate the model view and controller module for area luminosity. */
object AreaLuminosityMVC:

  /** Create a new [[AreaLuminosityMVCImpl]].
    * @param areaModel
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    * @return
    *   a new instance of [[AreaLuminosityMVCImpl]].
    */
  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaLuminosityMVCImpl =
    AreaLuminosityMVCImpl(areaModel, updateStateMessage)

  /** Implementation of the area luminosity MVC.
    * @param model
    *   the model of the area
    * @param updateStateMessage
    *   a function for update the area status and messages
    */
  class AreaLuminosityMVCImpl(model: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaLuminosityViewModule.Interface
      with AreaLuminosityControllerModule.Interface
      with AreaParametersMVC:

    override val areaModel: AreaModel = model
    override val parameterController: AreaParametersController = AreaLuminosityControllerImpl(updateStateMessage)
    override val parameterView: AreaParametersView = AreaLuminosityViewImpl()

    parameterController.initializeView(parameterView)
