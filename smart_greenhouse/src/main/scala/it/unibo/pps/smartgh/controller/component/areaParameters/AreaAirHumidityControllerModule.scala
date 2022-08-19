package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParameterController.{
  AbstractAreaParameterController,
  AreaParameterController
}
import it.unibo.pps.smartgh.model.area.{AreaAtomiseState, AreaModelModule, AreaVentilationState}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule

object AreaAirHumidityControllerModule:

  trait AreaAirHumidityControllerController extends AreaParameterController:
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
        extends AbstractAreaParameterController(areaModel, "Humidity")
        with AreaAirHumidityControllerController:

      override def getOptimalValues: (Double, Double) = (sensor.min, sensor.max)

      override def activateVentilation(): Unit =
        areaModel.updVentilationState(AreaVentilationState.VentilationActive)

      override def deactivateVentilation(): Unit =
        areaModel.updVentilationState(AreaVentilationState.VentilationInactive)

      override def atomiseArea(): Unit =
        areaModel.updAtomizeState(AreaAtomiseState.AtomisingActive)

      override def disableAtomiseArea(): Unit =
        areaModel.updAtomizeState(AreaAtomiseState.AtomisingInactive)

      override def updateValues(): Unit =
        updateStateMessage(sensor.message, sensor.status == SensorStatus.ALARM)
        areaAirHumidityView.updateCurrentValue(sensor.actualVal, sensor.status.toString)

  trait Interface extends Provider with Component:
    self: Requirements =>
