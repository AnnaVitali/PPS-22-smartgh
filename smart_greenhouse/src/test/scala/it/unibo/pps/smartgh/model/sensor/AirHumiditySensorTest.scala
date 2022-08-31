package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.{AreaAtomiseState, AreaComponentsState, AreaGatesState, AreaVentilationState}
import it.unibo.pps.smartgh.model.sensor.AirHumiditySensor.AirHumiditySensorImpl
import it.unibo.pps.smartgh.model.time.Timer
import monix.execution.Ack.Continue
import monix.execution.Scheduler.Implicits.global
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject
import org.apache.commons.lang3.time.DurationFormatUtils
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Milliseconds, Span}

import scala.concurrent.duration.*
import scala.language.postfixOps

/** This class contains the tests realized to verify that [[AirHumiditySensor]] behaves correctly. */
class AirHumiditySensorTest extends AnyFunSuite with Matchers with Eventually with BeforeAndAfter:

  private var areaComponentsState: AreaComponentsStateImpl = _
  private val timer = Timer(1 day)
  private val initialHumidity: Double = 90.0
  private var humiditySensor: AirHumiditySensorImpl = _
  private val subjectEnvironment = ConcurrentSubject[Double](MulticastStrategy.publish)
  private val subjectActions = ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)
  private val subjectTimer = ConcurrentSubject[String](MulticastStrategy.publish)

  private def setupTimer(tickPeriod: FiniteDuration): Unit =
    timer.changeTickPeriod(tickPeriod)

  private def initialValueTest(): Unit =
    subjectEnvironment.onNext(initialHumidity)
    eventually(timeout(Span(8000, Milliseconds))) {
      humiditySensor.getCurrentValue should be < initialHumidity
    }

  before {
    areaComponentsState = AreaComponentsState()
    areaComponentsState.gatesState = AreaGatesState.Close
    humiditySensor = AirHumiditySensor(
      areaComponentsState,
      (callback: String => Unit) =>
        subjectTimer.subscribe(
          s => {
            callback(s)
            Continue
          },
          _.printStackTrace(),
          () => {}
        )
    )
    humiditySensor.setObserverEnvironmentValue(subjectEnvironment)
    humiditySensor.setObserverActionsArea(subjectActions)
    subjectActions.onNext(areaComponentsState)
    timer.start(
      t => subjectTimer.onNext(DurationFormatUtils.formatDuration(t.toMillis, "HH:mm:ss", true)),
      println("time is up!")
    )
  }

  after {
    timer.stop()
  }

  test("The air humidity should initialize with the environment value minos a random value") {
    initialValueTest()
  }

  test("The air humidity value should decrease because the ventilation and the humidity are inactive") {
    setupTimer(500 microseconds)
    initialValueTest()

    eventually(timeout(Span(8000, Milliseconds))) {
      humiditySensor.getCurrentValue should be < initialHumidity
    }
  }

  test("The air humidity value should decrease if the ventilation is active") {
    initialValueTest()
    Thread.sleep(2000)

    areaComponentsState.ventilationState = AreaVentilationState.VentilationActive
    subjectActions.onNext(areaComponentsState)

    eventually(timeout(Span(8000, Milliseconds))) {
      humiditySensor.getCurrentValue should be < initialHumidity
    }
  }

  test("The air humidity value should increase if the atomiser is active") {
    initialValueTest()
    Thread.sleep(2000)

    val humidityValue = humiditySensor.getCurrentValue
    areaComponentsState.atomisingState = AreaAtomiseState.AtomisingActive
    subjectActions.onNext(areaComponentsState)

    eventually(timeout(Span(8000, Milliseconds))) {
      humiditySensor.getCurrentValue should be > humidityValue
    }
  }
