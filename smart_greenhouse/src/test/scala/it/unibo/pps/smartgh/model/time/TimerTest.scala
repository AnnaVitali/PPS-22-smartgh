package it.unibo.pps.smartgh.model.time

import it.unibo.pps.smartgh.model.time.Timer
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Seconds, Span, Milliseconds}

import scala.concurrent.duration.*
import scala.language.postfixOps

/** This class contains the tests realized to verify that the [[Timer]] work correctly. */
class TimerTest extends AnyFunSuite with Matchers with BeforeAndAfter with Eventually:

  private val duration = 5 seconds
  private var timer: Timer = _
  private var timerValue: FiniteDuration = _
  private var finish: Boolean = _

  before {
    timerValue = 0.seconds
    finish = false
    timer = Timer(duration)
    timer.start(this.finish = true)
    timer.addCallback(timerValue = _, 1)
  }

  after {
    timer.stop()
  }

  test("The timer should start when started") {
    eventually(timeout(Span(duration.toSeconds, Seconds))) {
      timerValue should be > 0.seconds
    }
  }

  test("When the timer is finished it should have the value at the setted duration") {
    eventually(timeout(Span(duration.toSeconds + 2, Seconds))) {
      finish shouldEqual true
    }
    timerValue shouldEqual duration
  }

  test("When the timer stops, its value should stops") {
    Thread.sleep(duration.toMillis / 2)
    timer.stop()
    val stoppedValue = timerValue
    Thread.sleep(duration.toMillis / 2)
    timerValue shouldEqual stoppedValue
  }

  test("When the speed is increased, the timer should finish earlier") {
    timer.changeTickPeriod(200 milliseconds)
    eventually(timeout(Span(duration.toSeconds / 2, Seconds))) {
      finish shouldEqual true
    }
    timerValue shouldEqual duration
  }

  test("When change period, the timer should continue its value") {
    val valueBefore = timerValue
    val period: FiniteDuration = 200.milliseconds
    timer.changeTickPeriod(period)
    eventually(timeout(Span(period.toMillis * 2, Milliseconds))) {
      timerValue shouldEqual (valueBefore + 1.seconds)
    }
  }
