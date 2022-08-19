package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaAirHumidityControllerModule
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaAirHumidityControllerModule.AreaAirHumidityControllerController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule.AreaAirHumidityView

object AreaAirHumidityMVC:

  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaAirHumidityMVCImpl =
    AreaAirHumidityMVCImpl(areaModel, updateStateMessage)

  class AreaAirHumidityMVCImpl(model: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaAirHumidityViewModule.Interface
      with AreaAirHumidityControllerModule.Interface:

    override val areaModel: AreaModel = model
    override val areaAirHumidityController: AreaAirHumidityControllerController = AreaAirHumidityControllerImpl(
      updateStateMessage
    )
    override val areaAirHumidityView: AreaAirHumidityView = AreaAirHumidityViewImpl()

    areaAirHumidityController.initializeView()
