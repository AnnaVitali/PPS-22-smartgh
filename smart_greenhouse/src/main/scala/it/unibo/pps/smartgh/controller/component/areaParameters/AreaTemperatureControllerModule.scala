package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaTemperatureViewModule
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaShieldState}

object AreaTemperatureControllerModule:

  trait AreaTemperatureController:
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

    class AreaTemperatureControllerImpl() extends AreaTemperatureController:
      private val soilMoistureSensor = areaModel.sensors.find(_.name == "Temperature").orNull

      override def updTempValue(value: Double): Unit = areaModel.updTemperature(value)
      override def openGates(): Unit = areaModel.updGateState(AreaGatesState.Open)
      override def closeGates(): Unit = areaModel.updGateState(AreaGatesState.Close)

  trait Interface extends Provider with Component:
    self: Requirements =>
