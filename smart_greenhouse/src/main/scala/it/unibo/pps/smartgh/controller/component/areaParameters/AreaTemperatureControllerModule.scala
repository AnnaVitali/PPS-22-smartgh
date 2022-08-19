package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParameterController.{
  AbstractAreaParameterController,
  AreaParameterController
}
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaTemperatureViewModule
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaShieldState}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import monix.reactive.Observable

import concurrent.duration.DurationInt
import monix.execution.Scheduler.Implicits.global

object AreaTemperatureControllerModule:

  trait AreaTemperatureController extends AreaParameterController:

    def initialValue: Double
    /** Update the temperature inside the area.
      * @param value
      *   New temperature value
      */
    def updTempValue(value: Double): Unit

    /** Open the area gates. */
    def openGates(): Unit

    /** Close the area gates. */
    def closeGates(): Unit

  trait Provider:
    val areaTemperatureController: AreaTemperatureController

  type Requirements = AreaTemperatureViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaTemperatureControllerImpl() extends AbstractAreaParameterController with AreaTemperatureController:
      private val temperatureSensor = areaModel.sensors.find(_.name.contentEquals("Temperature")).orNull

      timeoutUpd = Observable
        .interval(3.seconds)
        .map { _ =>
//          updateStateMessage(temperatureSensor.message, temperatureSensor.status == SensorStatus.ALARM)
          areaTemperatureView.updateCurrentValue(temperatureSensor.actualVal, temperatureSensor.status.toString)
        }

      override def initialValue: Double = 27.0 //todo

      override def getOptimalValues: (Double, Double) = (temperatureSensor.min, temperatureSensor.max)
//      override def currentValue: Double = temperatureSensor.actualVal
      override def updTempValue(value: Double): Unit = areaModel.updTemperature(value)
      override def openGates(): Unit = areaModel.updGateState(AreaGatesState.Open)
      override def closeGates(): Unit = areaModel.updGateState(AreaGatesState.Close)

  trait Interface extends Provider with Component:
    self: Requirements =>
