package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.{
  AbstractAreaParametersController,
  AreaParametersController
}
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaModelModule, AreaShieldState}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.areaParameters.AreaLuminosityViewModule.AreaLuminosityView
import it.unibo.pps.smartgh.view.component.areaParameters.{AreaLuminosityViewModule, AreaParametersView}
import org.scalactic.TripleEquals.convertToEqualizer

/** Object that encloses the controller module for the area luminosity parameter. */
object AreaLuminosityControllerModule:

  /** A trait that represents the area air luminosity controller parameter. */
  trait AreaLuminosityController extends AreaParametersController:

    /** Update the lamp brightness value.
      * @param value
      *   that represents the lamp brightness
      */
    def updLampValue(value: Double): Unit

    /** Get the lamp value.
      * @return
      *   the regulated lamp value
      */
    def getLampValue: Double

    /** Put down the area shields */
    def shieldsDown(): Unit

    /** Put up the area shields */
    def shieldsUp(): Unit

    /** Get the shielding state.
      * @return
      *   true if the shields is down, false otherwise
      */
    def isShielded: Boolean

  /** Trait that represents the provider of the controller for the area luminosity parameter. */
  trait Provider:

    /** The controller of area luminosity parameter. */
    val parameterController: AreaParametersController

  /** The controller requirements. */
  type Requirements = AreaLuminosityViewModule.Provider with AreaModelModule.Provider

  /** Trait that represent the controller component for the area luminosity parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaLuminosityController]] implementation.
      * @param updateStateMessage
      *   a function for update states message.
      */
    class AreaLuminosityControllerImpl(updateStateMessage: (String, Boolean) => Unit)
        extends AbstractAreaParametersController("Brightness", areaModel, updateStateMessage)
        with AreaLuminosityController:

      override def updLampValue(value: Double): Unit =
        areaModel.updBrightnessOfLamp(value)

      override def getLampValue: Double = areaModel.getBrightnessOfLamp

      override def shieldsDown(): Unit = areaModel.updShieldState(AreaShieldState.Down)

      override def shieldsUp(): Unit = areaModel.updShieldState(AreaShieldState.Up)

      override def isShielded: Boolean = areaModel.isShielded

      override protected def updateValues(view: AreaParametersView.AreaParametersView): Unit =
        super.updateValues(view)
        view.asInstanceOf[AreaLuminosityView].setUpActions(areaModel.gatesState === AreaGatesState.Open)

  /** Trait that combine provider and component for area luminosity parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>
