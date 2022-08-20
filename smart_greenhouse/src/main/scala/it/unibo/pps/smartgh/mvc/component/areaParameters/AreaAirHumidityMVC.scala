package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.{
  AreaAirHumidityControllerModule,
  AreaParametersController
}
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaAirHumidityControllerModule.AreaAirHumidityController
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.AreaParametersController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameters.{AreaAirHumidityViewModule, AreaParametersView}
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule.AreaAirHumidityView
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.AreaParametersView

object AreaAirHumidityMVC:

  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaAirHumidityMVCImpl =
    AreaAirHumidityMVCImpl(areaModel, updateStateMessage)

  class AreaAirHumidityMVCImpl(model: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaAirHumidityViewModule.Interface
      with AreaAirHumidityControllerModule.Interface
      with AreaParametersMVC:

    override val areaModel: AreaModel = model
    override val parameterController: AreaParametersController = AreaAirHumidityControllerImpl(updateStateMessage)
    override val parameterView: AreaParametersView = AreaAirHumidityViewImpl()

    parameterController.initializeView(parameterView)
