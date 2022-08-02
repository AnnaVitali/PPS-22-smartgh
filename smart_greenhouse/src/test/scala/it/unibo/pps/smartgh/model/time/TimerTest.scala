package it.unibo.pps.smartgh.model.time

import it.unibo.pps.smartgh.model.time.Timer
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Seconds, Span}

import scala.concurrent.duration.*
import scala.language.postfixOps

/** This class contains the tests realized to verify that the [[Timer]] work correctly. */
class TimerTest extends AnyFunSuite with Matchers with BeforeAndAfter with Eventually:

  private val duration = 5 seconds
  private var timer: Timer = _

  before {
    timer = Timer(duration)
  }

  test("The timer should not start before calling start") {
    timer.value shouldEqual 0.seconds
  }

  test("The timer should start when started") {
    timer.start(println(_))
    eventually(timeout(Span(duration.toSeconds, Seconds))) {
      timer.value should be > 0.seconds
    }
  }

  test("When the timer is finished it should have the value at the setted duration") {
    timer.start(println(_))
    eventually(timeout(Span(duration.toSeconds + 2, Seconds))) {
      timer.value shouldEqual duration
    }
  }

  test("When the timer stops, its value should stops") {
    timer.start(println(_))
    Thread.sleep(2000)
    timer.stop()
    val value = timer.value
    Thread.sleep(2000)
    timer.value shouldEqual value
  }

  test("When the speed is increased, the timer should finish earlier") {
    timer.start(println(_))
    timer.changeTickPeriod(200 milliseconds)
    eventually(timeout(Span(duration.toSeconds / 2, Seconds))) {
      timer.value shouldEqual duration
    }
  }
