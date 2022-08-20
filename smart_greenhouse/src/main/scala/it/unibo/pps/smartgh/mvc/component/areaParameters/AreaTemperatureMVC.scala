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

object AreaTemperatureMVC:

  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaTemperatureMVCImpl =
    AreaTemperatureMVCImpl(areaModel, updateStateMessage)

  class AreaTemperatureMVCImpl(model: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaTemperatureViewModule.Interface
      with AreaTemperatureControllerModule.Interface
      with AreaParametersMVC:

    override val areaModel: AreaModel = model
    override val parameterController: AreaParametersController = AreaTemperatureControllerImpl(updateStateMessage)
    override val parameterView: AreaParametersView = AreaTemperatureViewImpl()

    parameterController.initializeView(parameterView)
