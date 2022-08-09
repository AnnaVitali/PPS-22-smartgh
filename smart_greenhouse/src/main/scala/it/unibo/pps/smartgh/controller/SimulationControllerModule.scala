package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.controller.EnvironmentControllerModule.EnvironmentController
import it.unibo.pps.smartgh.controller.PlantSelectorControllerModule.Requirments
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule.EnvironmentModel
import it.unibo.pps.smartgh.model.time.{TimeModel, Timer}
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule.EnvironmentView
import it.unibo.pps.smartgh.view.SimulationViewModule

import scala.concurrent.duration.*
import scala.language.postfixOps
import scala.math.{exp, log, pow}
import org.apache.commons.lang3.time.DurationFormatUtils
import it.unibo.pps.smartgh.model.plants.Plant

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
    def notifyTimeValueChange(timeValue: FiniteDuration): Unit
    /** Method that is called when the simulation is finished. */
    //TODO doc
    def finishSimulation(): Unit

    var environmentView: EnvironmentView

    var environmentController: EnvironmentController

    var city: String

    var plantsSelected: List[Plant]

    def timer: Timer

  trait Provider:
    val simulationController: SimulationController

  trait Component:

    /** Object that can be used to create a new instance of [[TimeController]]. */
    class SimulationControllerImpl extends SimulationController:

      var lastRequestTime: Long = -1
      override var environmentView: EnvironmentView = _
      override var environmentController: EnvironmentController = _
      override var city: String = _
      override var plantsSelected: List[Plant] = _
      private val timeModel = TimeModel()
      override def timer = timeModel.timer
      timeModel.controller = this
      private val timeSpeed: Double => FiniteDuration = (x: Double) =>
        val x0 = 1
        val x1 = 10
        val y0 = pow(10, 6)
        val y1 = 50
        exp(((x - x0) / (x1 - x0)) * (log(y1) - log(y0)) + log(y0)) microseconds

      override def startSimulationTimer(): Unit = timeModel.start()

      override def stopSimulationTimer(): Unit = timeModel.stop()

      override def updateVelocityTimer(speed: Double): Unit = timeModel.setSpeed(timeSpeed(speed))

      override def notifyTimeValueChange(timeValue: FiniteDuration): Unit =
        val time: String = DurationFormatUtils.formatDuration(timeValue.toMillis, "HH:mm:ss", true)
        environmentView.displayElapsedTime(time)
        lastRequestTime = hasNewHourPassed(timeValue)

      override def finishSimulation(): Unit = environmentView.finishSimulation()

      private def hasNewHourPassed(timeValue: FiniteDuration): Long =
        if timeValue.toHours.>(lastRequestTime) then
          environmentController.notifyEnvironmentValuesChange(timeValue.toHours.intValue)
          timeValue.toHours
        else lastRequestTime

  trait Interface extends Provider with Component
