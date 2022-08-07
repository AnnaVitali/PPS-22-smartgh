package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.view.component.EnvironmentViewModule
import it.unibo.pps.smartgh.model.EnvironmentModelModule
import it.unibo.pps.smartgh.controller.TimeController

/** Object that encloses the controller module to manage ambient environment values and simulation time. */
object EnvironmentControllerModule:

  /** A trait that represents the controller for environment values and time management, it also allows their display in the view. */
  trait EnvironmentController:

    /** @return time controller. */
    def timeController : TimeController

    /** Method that notify the controller to start the simulation. */
    def startSimulation() : Unit

    /** Method that notify the controller to stop the simulation. */
    def stopSimulation() : Unit

    /** Method that notify the controller to request environment's model to update environment values.
      *   @param hour
      *     time value, expressed in hours, to which the update request refers.
      *   */
    def notifyEnvironmentValuesChange(hour: Int) : Unit

  /** Trait that represents the provider of the controller for environment values and time management. */
  trait Provider:
    val controller : EnvironmentController

  type Requirements = EnvironmentViewModule.Provider with EnvironmentModelModule.Provider

  /** Trait that represents the components of the controller for environment values and time management. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[EnvironmentController]] implementation. */
    class EnvironmentControllerImpl extends EnvironmentController:

      override def timeController: TimeController = TimeController(context.model.time, context.view, this)

      override def startSimulation(): Unit = timeController.startSimulationTimer()

      override def stopSimulation(): Unit = timeController.stopSimulationTimer()
      
      override def notifyEnvironmentValuesChange(hour: Int): Unit =
        context.view.displayNameCity(model.city.name)
        context.model.city.updateCurrentEnvironmentValues(hour)
        context.view.displayEnvironmentValues(model.city.currentEnvironmentValues)
        // notifySensors(currentEnvironmentValues)

      private def notifySensors(values: model.city.EnvironmentValues) : Unit = ???

  /** Trait that encloses the controller for environment values and time management. */
  trait Interface extends Provider with Component:
    self: Requirements =>
