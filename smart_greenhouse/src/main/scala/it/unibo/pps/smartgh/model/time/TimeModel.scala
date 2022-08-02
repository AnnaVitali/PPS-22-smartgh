package it.unibo.pps.smartgh.model.time

import org.apache.commons.lang3.time.DurationFormatUtils

import java.lang.module.FindException
import scala.concurrent.duration.*
import scala.language.postfixOps

trait TimeModel:

  def start(): Unit
  def setSpeed(speed: FiniteDuration): Unit
  def stop(): Unit

  type TimeController = Any

  def controller: TimeController
  def controller_=(controller: TimeController): Unit

object TimeModel:

  def apply(): TimeModel = TimeModelImpl()

  private class TimeModelImpl extends TimeModel:

    override var controller: TimeController = _
    private val endSimulation: FiniteDuration = 1 day
    private val timer: Timer = Timer(endSimulation)

    override def start(): Unit =
      timer.start(updateTime)

    override def setSpeed(speed: FiniteDuration): Unit =
      timer.changeTickPeriod(speed)

    override def stop(): Unit =
      timer.stop()

    private def updateTime(t: FiniteDuration): Unit =
      val time: String = DurationFormatUtils.formatDuration(t.toMillis, "HH:mm:ss", true)
      //todo: controller.updateTime(time)
      ???
