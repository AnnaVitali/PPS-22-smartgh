package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaTemperatureViewModule

object AreaTemperatureControllerModule:

  trait AreaTemperatureController

  trait Provider:
    val areaTemperatureController: AreaTemperatureController

  type Requirements = AreaTemperatureViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaTemperatureControllerImpl() extends AreaTemperatureController

  trait Interface extends Provider with Component:
    self: Requirements =>
