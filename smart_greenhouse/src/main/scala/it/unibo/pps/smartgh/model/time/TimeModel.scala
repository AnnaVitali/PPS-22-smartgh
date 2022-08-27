package it.unibo.pps.smartgh.model.time

import it.unibo.pps.smartgh.controller.SimulationControllerModule.SimulationController
import it.unibo.pps.smartgh.model.time.Timer
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.apache.commons.lang3.time.DurationFormatUtils

import scala.concurrent.duration.*
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.math.{exp, log, pow}

/** A trait that exposes methods to manage the time of the simulation, it represents its model. */
trait TimeModel:

  /** Start the time for a new simulation. */
  def start(): Unit

  /** Set the speed of the current simulation. */
  def setSpeed(speed: Double): Unit

  /** Stop the time of the current simulation. */
  def stop(): Unit

  /** Getter method for the controller component.
    * @return
    *   the controller associated to the model.
    */
  def controller: SimulationController

  /** Setter method for the controller component.
    * @param controller
    *   the controller associated to the model.
    */
  def controller_=(controller: SimulationController): Unit

/** Object that can be used to create a new instances of [[TimeModel]]. */
object TimeModel:

  /** Creates a new [[TimeModel]] object. */
  def apply(): TimeModel = TimeModelImpl()

  private class TimeModelImpl() extends TimeModel:

    //noinspection VarCouldBeVal
    override var controller: SimulationController = _
    private val endSimulation: FiniteDuration = 1 day
    private val timer: Timer = Timer(endSimulation)

    var lastRequestTime: Long = -1
    private val timeSpeed: Double => FiniteDuration = (x: Double) =>
      val x0 = 1
      val x1 = 10
      val y0 = pow(10, 6)
      val y1 = 100
      exp(((x - x0) / (x1 - x0)) * (log(y1) - log(y0)) + log(y0)) microseconds

    override def start(): Unit =
      timer.start(updateTimeValue, controller.finishSimulation())

    override def setSpeed(speed: Double): Unit =
      Task {
        timer.changeTickPeriod(timeSpeed(speed))
      }.executeAsync.runToFuture

    override def stop(): Unit =
      timer.stop()

    private def updateTimeValue(t: FiniteDuration): Unit =
      Task {
        val timeValue: String = DurationFormatUtils.formatDuration(t.toMillis, "HH:mm:ss", true)
        controller.notifyTimeValueChange(timeValue)
        lastRequestTime = Await.result(hasNewHourPassed(t), 60 seconds)
      }.executeAsync.runToFuture

    private def hasNewHourPassed(timeValue: FiniteDuration): Future[Long] =
      Task {
        if timeValue.toHours.>(lastRequestTime) then
          controller.notifyNewHourPassed(timeValue.toHours.intValue)
          timeValue.toHours
        else lastRequestTime
      }.executeAsync.runToFuture
