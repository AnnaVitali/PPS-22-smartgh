package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.{
  AbstractAreaParametersController,
  AreaParametersController
}
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaModelModule}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.areaParameters.AreaTemperatureViewModule
import org.scalactic.TripleEquals.convertToEqualizer

/** Object that encloses the controller module for the area temperature parameter. */
object AreaTemperatureControllerModule:

  /** A trait that represents the area temperature controller parameter. */
  trait AreaTemperatureController extends AreaParametersController:

    /** Get the current temperature.
      * @return
      *   the temperature value
      */
    def temperature: Double

    /** Update the temperature inside the area.
      * @param value
      *   New temperature value
      */
    def updTempValue(value: Double): Unit

    /** Specifies whether the gates is open.
      * @return
      *   true if the gates is open, false otherwise
      */
    def isGatesOpen: Boolean

    /** Open the area gates. */
    def openGates(): Unit

    /** Close the area gates. */
    def closeGates(): Unit

  /** Trait that represents the provider of the controller for the area temperature parameter. */
  trait Provider:
    /** The controller of area temperature parameter. */
    val parameterController: AreaParametersController

  /** The controller requirements. */
  type Requirements = AreaTemperatureViewModule.Provider with AreaModelModule.Provider

  /** Trait that represent the controller component for the area temperature parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaTemperatureController]] implementation.
      * @param updateStateMessage
      *   a function for update states message.
      */
    class AreaTemperatureControllerImpl(updateStateMessage: (String, Boolean) => Unit)
        extends AbstractAreaParametersController("Temperature", areaModel, updateStateMessage)
        with AreaTemperatureController:

      override def temperature: Double = areaModel.getAreaComponent.temperature
      override def updTempValue(value: Double): Unit = areaModel.updTemperature(value)
      override def isGatesOpen: Boolean = areaModel.getAreaComponent.gatesState === AreaGatesState.Open
      override def openGates(): Unit = areaModel.updGateState(AreaGatesState.Open)
      override def closeGates(): Unit = areaModel.updGateState(AreaGatesState.Close)

  /** Trait that combine provider and component for area air temperature parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>
