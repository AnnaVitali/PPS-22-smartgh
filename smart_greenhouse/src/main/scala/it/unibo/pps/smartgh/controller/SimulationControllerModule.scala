package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.controller.EnvironmentControllerModule.EnvironmentController
import it.unibo.pps.smartgh.controller.PlantSelectorControllerModule.Requirments
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule.EnvironmentModel
import it.unibo.pps.smartgh.model.time.{TimeModel, Timer}
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule.EnvironmentView
import it.unibo.pps.smartgh.view.SimulationViewModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.city.Environment

/** A trait that represents the controller for the simulation timer. */
object SimulationControllerModule:

  trait SimulationController:
    /** Method that notify the controller to start the simulation timer. */
    def startSimulationTimer(): Unit

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
    /** Method that is called when the simulation is finished. */

    /** Method that notify new hour passed.
      * @param timeValue
      *   new hour passed.
      */
    def notifyNewHourPassed(hour : Int) : Unit

    /** Method that is called when the simulation is finished. */
    def finishSimulation(): Unit
    
    def resetSimulation() : Unit

    var environmentController: EnvironmentController

    var environment: Environment

    var plantsSelected: List[Plant]

    var timer: Timer

  trait Provider:
    val simulationController: SimulationController

  trait Component:

    /** Object that can be used to create a new instance of [[TimeController]]. */
    class SimulationControllerImpl extends SimulationController:

      override var environmentController: EnvironmentController = _
      override var environment: Environment = _
      override var plantsSelected: List[Plant] = _

      private var timeModel = TimeModel()
      override var timer = timeModel.timer
      timeModel.controller = this

      override def startSimulationTimer(): Unit = timeModel.start()

      override def stopSimulationTimer(): Unit = timeModel.stop()

      override def updateVelocityTimer(speed: Double): Unit = timeModel.setSpeed(speed)

      override def notifyTimeValueChange(timeValue: String): Unit = environmentController.notifyTimeValueChange(timeValue)

      override def notifyNewHourPassed(hour: Int): Unit = environmentController.notifyEnvironmentValuesChange(hour)

      override def finishSimulation(): Unit = environmentController.finishSimulation()

      override def resetSimulation(): Unit =
        timeModel = TimeModel()
        timer = timeModel.timer
        timeModel.controller = this
        environment = null
        plantsSelected = List.empty
  
  trait Interface extends Provider with Component
