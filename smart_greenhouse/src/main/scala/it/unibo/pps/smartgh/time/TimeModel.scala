package it.unibo.pps.smartgh.time

import java.lang.module.FindException
import scala.concurrent.duration.*

trait TimeModel:

  def start: Unit
  def setSpeed(speed: FiniteDuration): Unit
  def stop: Unit

  type TimeController = Any

  def controller: TimeController
  def controller_=(controller: TimeController): Unit

object TimeModel:

  def apply(): TimeModel = TimeModelImpl()

  private class TimeModelImpl extends TimeModel:

    var _controller: TimeController = null
    val endSimulation: FiniteDuration = 23.hours + 59.minutes + 59.seconds
    private val timer: Timer

    override def controller: TimeController = _controller
    override def controller_=(controller: TimeController): Unit = _controller = controller

    override def start: Unit =
      timer = Timer()
      timer.subscribe(t => updateTime(t))

    override def setSpeed(speed: FiniteDuration): Unit =
      timer.changeSpeed(speed)

    override def stop: Unit = ???

    private def updateTime(t: FiniteDuration) = ???