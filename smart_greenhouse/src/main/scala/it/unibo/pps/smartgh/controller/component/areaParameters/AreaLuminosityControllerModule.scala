package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParameterController.{
  AbstractAreaParameterController,
  AreaParameterController
}
import it.unibo.pps.smartgh.controller.component.SceneController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaShieldState
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.BaseView
import it.unibo.pps.smartgh.view.component.areaParameters.AreaLuminosityViewModule
import monix.reactive.Observable

import concurrent.duration.DurationInt
import monix.execution.Scheduler.Implicits.global

object AreaLuminosityControllerModule:

  trait AreaLuminosityController extends AreaParameterController:

    /** Update the lamp brightness value
      * @param value
      *   that represents the lamp brightness
      */
    def updLampValue(value: Double): Unit

    /** Put down the area shields */
    def shieldsDown(): Unit

    /** Put up the area shields */
    def shieldsUp(): Unit

  trait Provider:
    val areaLuminosityController: AreaLuminosityController

  type Requirements = AreaLuminosityViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaLuminosityControllerImpl(updateStateMessage: (String, Boolean) => Unit)
        extends AbstractAreaParameterController
        with AreaLuminosityController:
      private val lumSensor = areaModel.sensors.find(_.name.contentEquals("Brightness")).orNull

      timeoutUpd = Observable
        .interval(3.seconds)
        .map { _ =>
          updateStateMessage(lumSensor.message, lumSensor.status == SensorStatus.ALARM)
          areaLuminosityView.updateCurrentValue(lumSensor.actualVal, lumSensor.status.toString)
        }

      override def getOptimalValues: (Double, Double) = (lumSensor.min, lumSensor.max)

      override def updLampValue(value: Double): Unit = areaModel.updBrightnessOfLamp(value)

      override def shieldsDown(): Unit = areaModel.updShieldState(AreaShieldState.Down)

      override def shieldsUp(): Unit = areaModel.updShieldState(AreaShieldState.Up)

  trait Interface extends Provider with Component:
    self: Requirements =>
