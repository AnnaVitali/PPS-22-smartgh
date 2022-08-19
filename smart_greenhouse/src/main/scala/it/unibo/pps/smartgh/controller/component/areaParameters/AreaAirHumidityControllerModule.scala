package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParameterController.{
  AbstractAreaParameterController,
  AreaParameterController
}
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule
import it.unibo.pps.smartgh.model.area.{AreaAtomiseState, AreaVentilationState}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import monix.execution.Cancelable
import monix.reactive.Observable

import concurrent.duration.DurationInt
import monix.execution.Scheduler.Implicits.global

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

    class AreaAirHumidityControllerImpl()
        extends AbstractAreaParameterController
        with AreaAirHumidityControllerController:
      private val humSensor = areaModel.sensors.find(_.name.contentEquals("Humidity")).orNull

      timeoutUpd = Observable
        .interval(3.seconds)
        .map { _ =>
//          updateStateMessage(humSensor.message, humSensor.status == SensorStatus.ALARM)
          areaAirHumidityView.updateCurrentValue(humSensor.actualVal, humSensor.status.toString)
        }

      override def getOptimalValues: (Double, Double) = (humSensor.min, humSensor.max)

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
