package it.unibo.pps.smartgh.timer

import monix.execution.{Ack, Cancelable, CancelableFuture}

import scala.concurrent.duration.*
import monix.reactive.{Observable, OverflowStrategy}
import monix.execution.Scheduler.Implicits.global
import monix.execution.schedulers.TestScheduler
import monix.eval.Task
import org.joda.time.{DateTime, Interval}

import java.util.concurrent.TimeUnit
import scala.concurrent.Future
import scala.concurrent.duration.Duration.Inf.toSeconds
import scala.language.postfixOps
import math.Integral.Implicits.infixIntegralOps

trait Timer:
  def value: Duration
  def start(task: Duration => Unit): Unit
  def changeSpeed(speed: FiniteDuration): Unit
  def stop(): Unit

object Timer:

  def apply(duration: Duration): Timer = TimerImpl(duration)

  private class TimerImpl(duration: Duration) extends Timer:
    var value: Duration = 0 seconds
    var cancelable: Cancelable = _
    var consumer: Duration => Unit = _

    override def start(task: Duration => Unit): Unit =
      consumer = t =>
        value = t
        task(t)
      cancelable = timer(value, 1 seconds).runToFuture

    override def changeSpeed(s: FiniteDuration): Unit =
      stop()
      cancelable = timer(value + 1.seconds, s).runToFuture

    override def stop(): Unit =
      cancelable.cancel()

    private def timer(from: Duration, speed: FiniteDuration): Task[Unit] =
      Observable
        .fromIterable(from.toSeconds to duration.toSeconds)
        .throttle(speed, 1)
        .map(Duration(_, TimeUnit.SECONDS))
        .foreachL(consumer)

@main def test(): Unit =
  val timer = Timer(1.day)
  timer.start(s =>
    println(
      "%d:%02d:%02d".formatted(s.toSeconds / 3600, (s.toSeconds % 3600) / 60, s.toSeconds % 60)
    )
  )
  Thread.sleep(5000)

  println("change speed-------------")
  timer.changeSpeed(100 milliseconds)
  Thread.sleep(5000)

  println("change speed-------------")
  timer.changeSpeed(10 milliseconds)
  Thread.sleep(5000)

  println("change speed-------------")
  timer.changeSpeed(1 nanoseconds)

  Thread.sleep(5000)
  timer.stop()
  println("stop")
