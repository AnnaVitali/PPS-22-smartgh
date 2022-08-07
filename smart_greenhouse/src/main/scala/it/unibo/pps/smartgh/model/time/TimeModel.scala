package it.unibo.pps.smartgh.model.time

import it.unibo.pps.smartgh.controller.TimeController
import org.apache.commons.lang3.time.DurationFormatUtils
import java.lang.module.FindException
import scala.concurrent.duration.*
import scala.language.postfixOps
import it.unibo.pps.smartgh.model.time.Timer

/** A trait that exposes methods to manage the time of the simulation, it represents its model. */
trait TimeModel:

  /** Start the time for a new simulation. */
  def start(): Unit

  /** Set the speed of the current simulation. */
  def setSpeed(speed: FiniteDuration): Unit

  /** Stop the time of the current simulation. */
  def stop(): Unit

  /** Getter method for the controller component.
    * @return
    *   the controller assoociated to the model.
    */
  def controller: TimeController

  /** Setter method for the controller component.
    * @param controller
    *   the controller assoociated to the model.
    */
  def controller_=(controller: TimeController): Unit

/** Object that can be used to create a new instances of [[TimeModel]]. */
object TimeModel:

  /** Creates a new [[TimeModel]] object. */
  def apply(): TimeModel = TimeModelImpl()

  private class TimeModelImpl() extends TimeModel:

    override var controller: TimeController = _
    private val endSimulation: FiniteDuration = 1 day
    private val timer: Timer = Timer(endSimulation)

    override def start(): Unit =
      timer.start(controller.finishSimulation())
      timer.addCallback(updateTimeValue, 1)

    override def setSpeed(speed: FiniteDuration): Unit =
      timer.changeTickPeriod(speed)

    override def stop(): Unit =
      timer.stop()

    private def updateTimeValue(t: FiniteDuration): Unit =
      controller.notifyTimeValueChange(t)
