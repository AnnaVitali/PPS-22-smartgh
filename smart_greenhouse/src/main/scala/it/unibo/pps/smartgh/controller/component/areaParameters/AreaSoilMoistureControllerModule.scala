package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParameterController.{
  AbstractAreaParameterController,
  AreaParameterController
}
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaHumidityState, AreaModelModule}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.areaParameters.AreaSoilMoistureViewModule

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
        extends AbstractAreaParameterController(areaModel, "Soil moisture")
        with AreaSoilMoistureController:

      override def getOptimalValues: (Double, Double) = (sensor.min, sensor.max)

      override def openGates(): Unit = areaModel.updGateState(AreaGatesState.Open)

      override def closeGates(): Unit = areaModel.updGateState(AreaGatesState.Close)

      override def movingSoil(): Unit = areaModel.updHumidityAction(AreaHumidityState.MovingSoil)

      override def watering(): Unit = areaModel.updHumidityAction(AreaHumidityState.Watering)

      override def noAction(): Unit = areaModel.updHumidityAction(AreaHumidityState.None)

      override def updateValues(): Unit =
        updateStateMessage(sensor.message, sensor.status == SensorStatus.ALARM)
        areaSoilMoistureView.updateCurrentValue(sensor.actualVal, sensor.status.toString)

  trait Interface extends Provider with Component:
    self: Requirements =>
