package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaSoilMoistureViewModule
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaHumidityState}

object AreaSoilMoistureControllerModule:

  trait AreaSoilMoistureController extends AreaParameterController:
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

  trait Provider:
    val areaSoilMoistureController: AreaSoilMoistureController

  type Requirements = AreaSoilMoistureViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaSoilMoistureControllerImpl() extends AreaSoilMoistureController:
      private val soilMoistureSensor = areaModel.sensors.find(_.name.contentEquals("Soil moisture")).orNull

      override def getOptimalValues: (Double, Double) = (soilMoistureSensor.min, soilMoistureSensor.max)

      override def openGates(): Unit = areaModel.updGateState(AreaGatesState.Open)

      override def closeGates(): Unit = areaModel.updGateState(AreaGatesState.Close)

      override def movingSoil(): Unit = areaModel.updHumidityAction(AreaHumidityState.MovingSoil)

      override def watering(): Unit = areaModel.updHumidityAction(AreaHumidityState.Watering)

      override def noAction(): Unit = areaModel.updHumidityAction(AreaHumidityState.None)

  trait Interface extends Provider with Component:
    self: Requirements =>
