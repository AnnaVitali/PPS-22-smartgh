package it.unibo.pps.smartgh.controller.component.areaParameter

import it.unibo.pps.smartgh.controller.component.areaParameter.AreaParameterController.{
  AbstractAreaParameterController,
  AreaParameterController
}
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaModelModule}
import it.unibo.pps.smartgh.view.component.areaParameter.AreaTemperatureViewModule
import org.scalactic.TripleEquals.convertToEqualizer

/** Object that encloses the controller module for the area temperature parameter. */
object AreaTemperatureControllerModule:

  /** A trait that represents the area temperature controller parameter. */
  trait AreaTemperatureController extends AreaParameterController:

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
    val parameterController: AreaParameterController

  /** The controller requirements. */
  type Requirements = AreaTemperatureViewModule.Provider with AreaModelModule.Provider

  /** Trait that represent the controller component for the area temperature parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaTemperatureController]] implementation.
      * @param updateStateMessage
      *   a function for update states message.
      */
    class AreaTemperatureControllerImpl(private val updateStateMessage: (String, Boolean) => Unit)
        extends AbstractAreaParameterController("Temperature", areaModel, updateStateMessage)
        with AreaTemperatureController:

      override protected val updateCurrentValue: (String, String) => Unit = parameterView.updateCurrentValue
      override protected val updateDescription: String => Unit = parameterView.updateDescription
      override def temperature: Double = areaModel.getAreaComponent.temperature
      override def updTempValue(value: Double): Unit = areaModel.updTemperature(value)
      override def isGatesOpen: Boolean = areaModel.getAreaComponent.gatesState === AreaGatesState.Open
      override def openGates(): Unit = areaModel.updGateState(AreaGatesState.Open)
      override def closeGates(): Unit = areaModel.updGateState(AreaGatesState.Close)

  /** Trait that combine provider and component for area air temperature parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>
