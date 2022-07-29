package it.unibo.pps.smartgh.timer

import monix.execution.CancelableFuture

import scala.concurrent.duration.*
import monix.reactive.{Observable, OverflowStrategy}
import monix.execution.Scheduler.Implicits.global
import monix.execution.schedulers.TestScheduler.Task
import org.joda.time.DateTime

import scala.language.postfixOps

trait Timer:
  def subscribe(callback: FiniteDuration => Unit): CancelableFuture[Unit]
  def changeSpeed(speed: FiniteDuration): Unit

object Timer:

  def apply(): Timer = TimerImpl()

  class TimerImpl extends Timer:
    var speed = 1 seconds

    val timer: Observable[FiniteDuration] =
      Observable
        .fromIterable(
          new Iterable[FiniteDuration]:
            override def iterator: Iterator[FiniteDuration] =
              new Iterator[FiniteDuration]:
                var time: FiniteDuration = 0.hours + 0.minutes + 0.seconds
                override def hasNext: Boolean = time != 23.hours + 59.minutes + 59.seconds
                override def next(): FiniteDuration =
                  time = time + 1.seconds
                  time
        )
        .delayOnNext(speed)
        .map(t => t)

    override def subscribe(callback: FiniteDuration => Unit): CancelableFuture[Unit] =
      timer.foreachL(callback).runToFuture

    override def changeSpeed(s: FiniteDuration): Unit =
      speed = s

@main def test =
  val timer = Timer()
  timer.subscribe(t => println(t))

  Thread.sleep(5000)

  println("change speed")
  timer.changeSpeed(500 milliseconds)

  Thread.sleep(50000)
