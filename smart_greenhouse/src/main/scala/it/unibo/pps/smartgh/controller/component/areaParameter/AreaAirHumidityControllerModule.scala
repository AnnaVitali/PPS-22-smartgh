package it.unibo.pps.smartgh.controller.component.areaParameter

import it.unibo.pps.smartgh.controller.component.areaParameter.AreaParameterController.{
  AbstractAreaParameterController,
  AreaParameterController
}
import it.unibo.pps.smartgh.model.area.{AreaAtomiseState, AreaModelModule, AreaVentilationState}
import it.unibo.pps.smartgh.view.component.areaParameter.AreaAirHumidityViewModule
import org.scalactic.TripleEquals.convertToEqualizer

/** Object that encloses the controller module for the area air humidity parameter. */
object AreaAirHumidityControllerModule:

  /** A trait that represents the area air humidity controller parameter. */
  trait AreaAirHumidityController extends AreaParameterController:

    /** Activate the area ventilation. */
    def activateVentilation(): Unit

    /** Deactivate the area ventilation. */
    def deactivateVentilation(): Unit

    /** Specifies whether the ventilator is activated.
      * @return
      *   true if is active, false otherwise
      */
    def isVentilationActivated: Boolean

    /** Atomise the area */
    def atomiseArea(): Unit

    /** Disable the atomisation of the area */
    def disableAtomiseArea(): Unit

    /** Specifies whether the atomiser is activated.
      * @return
      *   true if is active, false otherwise
      */
    def isAtomiserActivated: Boolean

  /** Trait that represents the provider of the controller for the area air humidity parameter. */
  trait Provider:

    /** The controller of area air humidity parameter. */
    val parameterController: AreaParameterController

  /** The controller requirements. */
  type Requirements = AreaAirHumidityViewModule.Provider with AreaModelModule.Provider

  /** Trait that represent the controller component for the area air humidity parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaAirHumidityController]] implementation.
      * @param updateStateMessage
      *   a function for update states message.
      */
    class AreaAirHumidityControllerImpl(private val updateStateMessage: (String, Boolean) => Unit)
        extends AbstractAreaParameterController("Humidity", areaModel, updateStateMessage)
        with AreaAirHumidityController:
      import it.unibo.pps.smartgh.model.area.AreaAtomiseState.{AtomisingActive, AtomisingInactive}
      import it.unibo.pps.smartgh.model.area.AreaVentilationState.{VentilationInactive, VentilationActive}

      override protected val updateCurrentValue: (String, String) => Unit = parameterView.updateCurrentValue
      override protected val updateDescription: String => Unit = parameterView.updateDescription
      override def activateVentilation(): Unit = areaModel.updVentilationState(VentilationActive)
      override def deactivateVentilation(): Unit = areaModel.updVentilationState(VentilationInactive)
      override def isVentilationActivated: Boolean = areaModel.getAreaComponent.ventilationState === VentilationActive
      override def atomiseArea(): Unit = areaModel.updAtomizeState(AtomisingActive)
      override def disableAtomiseArea(): Unit = areaModel.updAtomizeState(AtomisingInactive)
      override def isAtomiserActivated: Boolean = areaModel.getAreaComponent.atomisingState === AtomisingActive

  /** Trait that combine provider and component for area air humidity parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>
