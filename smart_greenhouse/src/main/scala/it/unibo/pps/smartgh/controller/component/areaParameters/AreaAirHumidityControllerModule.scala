package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.{
  AbstractAreaParametersController,
  AreaParametersController
}
import it.unibo.pps.smartgh.model.area.{AreaAtomiseState, AreaModelModule, AreaVentilationState}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule

object AreaAirHumidityControllerModule:

  trait AreaAirHumidityController extends AreaParametersController:

    /** Activate the area ventilation. */
    def activateVentilation(): Unit

    /** Deactivate the area ventilation. */
    def deactivateVentilation(): Unit

    /** Specifies whether the ventilator is activated.
      * @return
      *   true if is active, false otherwise
      */
    def isVentilationActivated: Boolean

    /** Atomise the area */
    def atomiseArea(): Unit

    /** Disable the atomisation of the area */
    def disableAtomiseArea(): Unit

    /** Specifies whether the atomiser is activated.
      * @return
      *   true if is active, false otherwise
      */
    def isAtomiserActivated: Boolean

  trait Provider:
    val parameterController: AreaParametersController

  type Requirements = AreaAirHumidityViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaAirHumidityControllerImpl(updateStateMessage: (String, Boolean) => Unit)
        extends AbstractAreaParametersController("Humidity", areaModel, updateStateMessage)
        with AreaAirHumidityController:

      override def activateVentilation(): Unit =
        areaModel.updVentilationState(AreaVentilationState.VentilationActive)

      override def deactivateVentilation(): Unit =
        areaModel.updVentilationState(AreaVentilationState.VentilationInactive)

      override def isVentilationActivated: Boolean =
        areaModel.ventilationState == AreaVentilationState.VentilationActive

      override def atomiseArea(): Unit =
        areaModel.updAtomizeState(AreaAtomiseState.AtomisingActive)

      override def disableAtomiseArea(): Unit =
        areaModel.updAtomizeState(AreaAtomiseState.AtomisingInactive)

      override def isAtomiserActivated: Boolean = areaModel.atomiserState == AreaAtomiseState.AtomisingActive

  trait Interface extends Provider with Component:
    self: Requirements =>
