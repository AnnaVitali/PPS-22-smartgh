package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaTemperatureControllerModule
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaTemperatureControllerModule.AreaTemperatureController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameters.AreaTemperatureViewModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaTemperatureViewModule.AreaTemperatureView

object AreaTemperatureMVC:

  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaTemperatureMVCImpl =
    AreaTemperatureMVCImpl(areaModel, updateStateMessage)

  class AreaTemperatureMVCImpl(model: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaTemperatureViewModule.Interface
      with AreaTemperatureControllerModule.Interface:

    override val areaModel: AreaModel = model
    override val areaTemperatureController: AreaTemperatureController = AreaTemperatureControllerImpl(
      updateStateMessage
    )
    override val areaTemperatureView: AreaTemperatureView = AreaTemperatureViewImpl()

    areaTemperatureController.initializeView()
