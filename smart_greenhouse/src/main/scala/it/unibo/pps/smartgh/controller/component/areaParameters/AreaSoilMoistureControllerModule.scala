package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.{
  AbstractAreaParametersController,
  AreaParametersController
}
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaHumidityState, AreaModelModule}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.areaParameters.AreaSoilMoistureViewModule

/** Object that encloses the controller module for the area soil moisture parameter. */
object AreaSoilMoistureControllerModule:

  /** A trait that represents the area soil humidity controller parameter. */
  trait AreaSoilMoistureController extends AreaParametersController:
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

  /** Trait that represents the provider of the controller for the area soil humidity parameter. */
  trait Provider:

    /** The controller of area soil moisture parameter. */
    val parameterController: AreaParametersController

  /** The controller requirements. */
  type Requirements = AreaSoilMoistureViewModule.Provider with AreaModelModule.Provider

  /** Trait that represent the controller component for the area soil moisture parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaSoilMoistureController]] implementation.
      * @param updateStateMessage
      *   a function for update states message.
      */
    class AreaSoilMoistureControllerImpl(updateStateMessage: (String, Boolean) => Unit)
        extends AbstractAreaParametersController("Soil moisture", areaModel, updateStateMessage)
        with AreaSoilMoistureController:

      override def openGates(): Unit = areaModel.updGateState(AreaGatesState.Open)

      override def closeGates(): Unit = areaModel.updGateState(AreaGatesState.Close)

      override def movingSoil(): Unit = areaModel.updHumidityAction(AreaHumidityState.MovingSoil)

      override def watering(): Unit = areaModel.updHumidityAction(AreaHumidityState.Watering)

      override def noAction(): Unit = areaModel.updHumidityAction(AreaHumidityState.None)

  /** Trait that combine provider and component for area soil moisture parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>
