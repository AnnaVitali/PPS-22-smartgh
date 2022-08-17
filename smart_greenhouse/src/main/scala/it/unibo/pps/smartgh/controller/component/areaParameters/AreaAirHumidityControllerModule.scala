package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule

object AreaAirHumidityControllerModule:

  trait AreaAirHumidityController

  trait Provider:
    val areaAirHumidityController: AreaAirHumidityController

  type Requirements = AreaAirHumidityViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaAirHumidityControllerImpl() extends AreaAirHumidityController

  trait Interface extends Provider with Component:
    self: Requirements =>
