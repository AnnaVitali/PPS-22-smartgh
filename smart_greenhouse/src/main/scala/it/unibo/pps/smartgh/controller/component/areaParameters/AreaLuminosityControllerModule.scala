package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParameterController.{
  AbstractAreaParameterController,
  AreaParameterController
}
import it.unibo.pps.smartgh.model.area.{AreaModelModule, AreaShieldState}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.areaParameters.AreaLuminosityViewModule

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
        extends AbstractAreaParameterController(areaModel, "Brightness")
        with AreaLuminosityController:

      override def getOptimalValues: (Double, Double) = (sensor.min, sensor.max)

      override def updLampValue(value: Double): Unit = areaModel.updBrightnessOfLamp(value)

      override def shieldsDown(): Unit = areaModel.updShieldState(AreaShieldState.Down)

      override def shieldsUp(): Unit = areaModel.updShieldState(AreaShieldState.Up)

      override def updateValues(): Unit =
        updateStateMessage(sensor.message, sensor.status == SensorStatus.ALARM)
        areaLuminosityView.updateCurrentValue(sensor.actualVal, sensor.status.toString)

  trait Interface extends Provider with Component:
    self: Requirements =>
