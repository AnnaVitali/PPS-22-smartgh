package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.{
  AbstractAreaParametersController,
  AreaParametersController
}
import it.unibo.pps.smartgh.model.area.{AreaAtomiseState, AreaModelModule, AreaVentilationState}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule

object AreaAirHumidityControllerModule:

  trait AreaAirHumidityControllerController extends AreaParametersController:
    /** Activate the area ventilation. */
    def activateVentilation(): Unit

    /** Deactivate the area ventilation. */
    def deactivateVentilation(): Unit

    /** Atomise the area */
    def atomiseArea(): Unit

    /** Disable the atomisation of the area */
    def disableAtomiseArea(): Unit

  trait Provider:
    val areaAirHumidityController: AreaAirHumidityControllerController

  type Requirements = AreaAirHumidityViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaAirHumidityControllerImpl(updateStateMessage: (String, Boolean) => Unit)
        extends AbstractAreaParametersController("Humidity", areaModel, updateStateMessage)
        with AreaAirHumidityControllerController:

      override def activateVentilation(): Unit =
        areaModel.updVentilationState(AreaVentilationState.VentilationActive)

      override def deactivateVentilation(): Unit =
        areaModel.updVentilationState(AreaVentilationState.VentilationInactive)

      override def atomiseArea(): Unit =
        areaModel.updAtomizeState(AreaAtomiseState.AtomisingActive)

      override def disableAtomiseArea(): Unit =
        areaModel.updAtomizeState(AreaAtomiseState.AtomisingInactive)

  trait Interface extends Provider with Component:
    self: Requirements =>
