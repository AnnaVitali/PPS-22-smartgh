package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaAirHumidityControllerModule.AreaAirHumidityController
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.AreaParametersController
import it.unibo.pps.smartgh.controller.component.areaParameters.{
  AreaAirHumidityControllerModule,
  AreaParametersController
}
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule.AreaAirHumidityView
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.AreaParametersView
import it.unibo.pps.smartgh.view.component.areaParameters.{AreaAirHumidityViewModule, AreaParametersView}

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
      with AreaParametersMVC:

    override val parameterController: AreaParametersController = AreaAirHumidityControllerImpl(updateStateMessage)
    override val parameterView: AreaParametersView = AreaAirHumidityViewImpl()

    parameterController.initializeView(parameterView)
