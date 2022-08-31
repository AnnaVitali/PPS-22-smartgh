package it.unibo.pps.smartgh.controller.component.areaParameter

import it.unibo.pps.smartgh.controller.component.areaParameter.AreaParameterController.{
  AbstractAreaParameterController,
  AreaParameterController
}
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaHumidityState, AreaModelModule}
import it.unibo.pps.smartgh.view.component.areaParameter.AreaSoilMoistureViewModule

/** Object that encloses the controller module for the area soil moisture parameter. */
object AreaSoilMoistureControllerModule:

  /** A trait that represents the area soil humidity controller parameter. */
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

  /** Trait that represents the provider of the controller for the area soil humidity parameter. */
  trait Provider:

    /** The controller of area soil moisture parameter. */
    val parameterController: AreaParameterController

  /** The controller requirements. */
  type Requirements = AreaSoilMoistureViewModule.Provider with AreaModelModule.Provider

  /** Trait that represent the controller component for the area soil moisture parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaSoilMoistureController]] implementation.
      * @param updateStateMessage
      *   a function for update states message.
      */
    class AreaSoilMoistureControllerImpl(private val updateStateMessage: (String, Boolean) => Unit)
        extends AbstractAreaParameterController("Soil moisture", areaModel, updateStateMessage)
        with AreaSoilMoistureController:

      override protected val updateCurrentValue: (String, String) => Unit = parameterView.updateCurrentValue
      override protected val updateDescription: String => Unit = parameterView.updateDescription
      override def openGates(): Unit = areaModel.updGateState(AreaGatesState.Open)
      override def closeGates(): Unit = areaModel.updGateState(AreaGatesState.Close)
      override def movingSoil(): Unit = areaModel.updHumidityAction(AreaHumidityState.MovingSoil)
      override def watering(): Unit = areaModel.updHumidityAction(AreaHumidityState.Watering)
      override def noAction(): Unit = areaModel.updHumidityAction(AreaHumidityState.None)

  /** Trait that combine provider and component for area soil moisture parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>
