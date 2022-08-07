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

  /** Current value of the timer. */
  def value: FiniteDuration

  /** Start the timer.
    * @param task
    *   a task that will consume by the timer at each tick.
    */
  def start(): Unit

  def addCallback(task: FiniteDuration => Unit, timeMustPass: Int): Unit

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
    *   the duration of the timer.
    * @return
    *   a new instance of [[Timer]]
    */
  def apply(duration: FiniteDuration): Timer = TimerImpl(duration)

  private class TimerImpl(duration: FiniteDuration) extends Timer:
    private var observable: Observable[FiniteDuration] = _
    private var period = 1 seconds
    var value: FiniteDuration = 0 seconds
//    private var cancelableList: Seq[Cancelable] = Seq()
    private var consumer: FiniteDuration => Unit = _
    private var callbacks: Map[(FiniteDuration => Unit, Int), Cancelable] = Map()

    override def start(): Unit =
      timer(value, period)
      callbacks = callbacks + (((t: FiniteDuration) => value = t, 1) ->
        observable
          .throttle(period, 1)
          .foreachL(value = _)
          .runToFuture)

    private def registerCallback(task: FiniteDuration => Unit, timeMustPass: Int): Cancelable =
      observable
        .map(_ * timeMustPass)
        .throttle(period * timeMustPass, 1)
        .foreachL(task)
        .runToFuture

    override def addCallback(task: FiniteDuration => Unit, timeMustPass: Int): Unit =
      callbacks = callbacks + ((task, timeMustPass) -> registerCallback(task, timeMustPass))

    override def changeTickPeriod(period: FiniteDuration): Unit =
      stop()
      timer(value + 1.seconds, period)
      callbacks.foreach(c => callbacks.updated(c._1, registerCallback(c._1._1, c._1._2)))

    override def stop(): Unit =
      callbacks.values.foreach(_.cancel())

    private def timer(from: FiniteDuration, period: FiniteDuration): Unit =
      observable = Observable
        .fromIterable(from.toSeconds to duration.toSeconds)
        .map(Duration(_, TimeUnit.SECONDS))

//        .throttle(period, 1)
//        .foreachL(consumer)

@main def testTimer =
  val timer: Timer = Timer(1 day)
  timer.start()
  timer.addCallback((t: FiniteDuration) => println("Callback1: " + t), 5)
  timer.addCallback((t: FiniteDuration) => println("Callback2: " + t), 2)

  Thread.sleep(50000)
