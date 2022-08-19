package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParameterController.{
  AbstractAreaParameterController,
  AreaParameterController
}
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaSoilMoistureViewModule
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaHumidityState}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import monix.execution.Cancelable
import monix.reactive.Observable

import concurrent.duration.DurationInt
import monix.execution.Scheduler.Implicits.global

object AreaSoilMoistureControllerModule:

  trait AreaSoilMoistureController extends AreaParameterController:
    def initializeView(): Unit
    /** Open the area gates. */
    def openGates(): Unit

    /** Close the area gates. */
    def closeGates(): Unit

    /** Moving the soil. */
    def movingSoil(): Unit

    /** Watering the area. */
    def watering(): Unit

    /** Define that no action is performed on the area. */
    def noAction(): Unit

    def stopListening(): Unit

  trait Provider:
    val areaSoilMoistureController: AreaSoilMoistureController

  type Requirements = AreaSoilMoistureViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaSoilMoistureControllerImpl(updateStateMessage: (String, Boolean) => Unit)
        extends AbstractAreaParameterController
        with AreaSoilMoistureController:
      private val soilMoistureSensor = areaModel.sensors.find(_.name.contentEquals("Soil moisture")).orNull

      timeoutUpd = Observable
        .interval(3.seconds)
        .map { _ =>
          updateStateMessage(soilMoistureSensor.message, soilMoistureSensor.status == SensorStatus.ALARM)
          areaSoilMoistureView.updateCurrentValue(soilMoistureSensor.actualVal, soilMoistureSensor.status.toString)
        }

      override def getOptimalValues: (Double, Double) = (soilMoistureSensor.min, soilMoistureSensor.max)

      override def openGates(): Unit = areaModel.updGateState(AreaGatesState.Open)

      override def closeGates(): Unit = areaModel.updGateState(AreaGatesState.Close)

      override def movingSoil(): Unit = areaModel.updHumidityAction(AreaHumidityState.MovingSoil)

      override def watering(): Unit = areaModel.updHumidityAction(AreaHumidityState.Watering)

      override def noAction(): Unit = areaModel.updHumidityAction(AreaHumidityState.None)

  trait Interface extends Provider with Component:
    self: Requirements =>
