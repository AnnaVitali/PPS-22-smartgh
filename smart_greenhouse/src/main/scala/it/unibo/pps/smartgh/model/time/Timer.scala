package it.unibo.pps.smartgh.model.time

import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import monix.execution.schedulers.TestScheduler
import monix.execution.{Ack, Cancelable, CancelableFuture}
import monix.reactive.{Observable, OverflowStrategy}
import org.apache.commons.lang3.time.DurationFormatUtils
import org.joda.time.{DateTime, Interval}

import java.util.concurrent.TimeUnit
import scala.concurrent.Future
import scala.concurrent.duration.*
import scala.concurrent.duration.Duration.Inf.toSeconds
import scala.language.postfixOps
import scala.math.Integral.Implicits.infixIntegralOps

/** A trait that exposing methods for a timer. */
trait Timer:

  /** Start the timer.
    * @param tickTask
    *   a task that will consume by the timer at each tick
    * @param finishTask
    *   a task that will consume by the timer when the timer is finished
    */
  def start(tickTask: FiniteDuration => Unit, finishTask: => Unit): Unit

  /** Change the period in which the timer emits a tick. For example, with a period of 2 seconds, the timer emits a
    * value every two seconds
    * @param period
    *   time that has to pass before emitting new items
    */
  def changeTickPeriod(period: FiniteDuration): Unit

  /** Stop the timer. */
  def stop(): Unit

/** Object that can used to create a new instances of [[Timer]]. */
object Timer:

  /** Creates a new [[Timer]] object. The timer starts from 0 until the specified duration.
    * @param duration
    *   the duration of the timer
    * @return
    *   a new instance of [[Timer]]
    */
  def apply(duration: FiniteDuration): Timer = TimerImpl(duration)

  private class TimerImpl(duration: FiniteDuration) extends Timer:
    private var value: FiniteDuration = 0 second
    private var cancelable: Cancelable = _
    private var consumer: FiniteDuration => Unit = _
    private var onFinishTask: Option[Throwable] => Task[Unit] = _

    override def start(tickTask: FiniteDuration => Unit, finishTask: => Unit): Unit =
      consumer = t => tickTask(t)
      onFinishTask = _ => Task(finishTask)
      cancelable = timer(value, 1 second).runToFuture

    override def changeTickPeriod(period: FiniteDuration): Unit =
      stop()
      cancelable = timer(value + 1.second, period).runToFuture

    override def stop(): Unit =
      cancelable.cancel()

    private def timer(from: FiniteDuration, period: FiniteDuration): Task[Unit] =
      Observable
        .fromIterable(from.toSeconds to duration.toSeconds)
        .throttle(period, 1)
        .map(Duration(_, TimeUnit.SECONDS))
        .foreachL(consumer)
        .doOnFinish(onFinishTask)
