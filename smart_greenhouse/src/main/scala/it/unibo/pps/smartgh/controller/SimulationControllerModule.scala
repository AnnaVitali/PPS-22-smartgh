package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.controller.component.EnvironmentControllerModule.EnvironmentController
import it.unibo.pps.smartgh.controller.component.PlantSelectorControllerModule.Requirements
import it.unibo.pps.smartgh.model.greenhouse.Environment
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule.EnvironmentModel
import it.unibo.pps.smartgh.model.time.{TimeModel, Timer}
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule.EnvironmentView
import it.unibo.pps.smartgh.view.SimulationViewModule
import it.unibo.pps.smartgh.model.plants.Plant

/** Object that encloses the controller module for the simulation. */
object SimulationControllerModule:

  /** A trait that represents the controller for the simulation. */
  trait SimulationController:

    /** Method that notify the controller to start the simulation timer. */
    def startSimulationTimer(): Unit

    /** Method that notify the controller to stop the simulation timer. */
    def stopSimulationTimer(): Unit

    /** Method that notify the controller to update the speed of the simulation timer.
      * @param speed
      *   time that has to pass before emitting new items.
      */
    def updateVelocityTimer(speed: Double): Unit

    /** Method that notify the controller the change of the value of simulation timer.
      * @param timeValue
      *   new simulation time value.
      */
    def notifyTimeValueChange(timeValue: String): Unit

    /** Method that notify new hour passed.
      * @param hour
      *   new hour passed.
      */
    def notifyNewHourPassed(hour: Int): Unit

    /** Method that is called when the simulation is finished. */
    def finishSimulation(): Unit

    /** Method for reset the simulation. */
    def resetSimulation(): Unit

    /** The environment controller of the application. */
    var environmentController: EnvironmentController

    /** The current environment of the simulation. */
    var environment: Environment

    /** The selected plants of the simulation. */
    var plantsSelected: List[Plant]

    /** The timer of the simulation. */
    var timer: Timer

  /** Trait that represents the provider of the controller for the simulation. */
  trait Provider:

    /** The controller of the simulation. */
    val simulationController: SimulationController

  /** Trait that represent the controller component of the simulation. */
  trait Component:

    /** Object that can be used to create a new instance of [[SimulationController]]. */
    class SimulationControllerImpl extends SimulationController:

      override var environmentController: EnvironmentController = _
      override var environment: Environment = _
      override var plantsSelected: List[Plant] = _

      private var timeModel = TimeModel()
      override var timer: Timer = timeModel.timer
      timeModel.controller = this

      override def startSimulationTimer(): Unit = timeModel.start()

      override def stopSimulationTimer(): Unit = timeModel.stop()

      override def updateVelocityTimer(speed: Double): Unit = timeModel.setSpeed(speed)

      override def notifyTimeValueChange(timeValue: String): Unit =
        environmentController.notifyTimeValueChange(timeValue)

      override def notifyNewHourPassed(hour: Int): Unit = environmentController.notifyEnvironmentValuesChange(hour)

      override def finishSimulation(): Unit = environmentController.finishSimulation()

      override def resetSimulation(): Unit =
        timeModel = TimeModel()
        timer = timeModel.timer
        timeModel.controller = this
        environment = null
        plantsSelected = List.empty

  /** Trait that combine provider and component for the simulation. */
  trait Interface extends Provider with Component
