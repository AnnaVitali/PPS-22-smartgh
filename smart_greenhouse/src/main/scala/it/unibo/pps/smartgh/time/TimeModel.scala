package it.unibo.pps.smartgh.time

import java.lang.module.FindException
import scala.concurrent.duration.*

trait TimeModel:

  def start: Unit
  def getTime: FiniteDuration
  def stop: Unit

object TimeModel:

  def apply(): TimeModel = TimeModelImpl()

  private class TimeModelImpl extends TimeModel:

    val start: FiniteDuration = 0.hours + 0.minutes + 0.seconds
    val end: FiniteDuration = 23.hours + 59.minutes + 59.seconds
    var elapsedTime = start

    private val timer = ???

    private def setTime(time: FiniteDuration) : Unit =
      elapsedTime = time

    override def start: Unit =
      //timer.subscribe(t => setTime(t))

    override def getTime: FiniteDuration =
      elapsedTime

    override def stop: Unit =
      elapsedTime = start
      //timer.stop()
