package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaSoilMoistureViewModule

object AreaSoilMoistureControllerModule:

  trait AreaSoilMoistureController

  trait Provider:
    val areaSoilMoistureController: AreaSoilMoistureController

  type Requirements = AreaSoilMoistureViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaSoilMoistureControllerImpl() extends AreaSoilMoistureController

  trait Interface extends Provider with Component:
    self: Requirements =>
