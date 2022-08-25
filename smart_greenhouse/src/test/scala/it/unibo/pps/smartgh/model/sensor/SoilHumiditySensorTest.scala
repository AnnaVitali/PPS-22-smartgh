package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.{AreaComponentsState, AreaGatesState, AreaHumidityState}
import it.unibo.pps.smartgh.model.sensor.SoilHumiditySensor.SoilHumiditySensorImpl
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

/** This class contains the tests realized to verify that [[SoilHumiditySensor]] behaves correctly. */
class SoilHumiditySensorTest extends AnyFunSuite with Matchers with Eventually with BeforeAndAfter:

  private var areaComponentsState: AreaComponentsStateImpl = _
  private val timer = Timer(1 day)
  private var humiditySensor: SoilHumiditySensorImpl = _
  private val subjectEnvironment = ConcurrentSubject[Double](MulticastStrategy.publish)
  private val subjectActions = ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)
  private val subjectTimer = ConcurrentSubject[String](MulticastStrategy.publish)

  private def setupTimer(tickPeriod: FiniteDuration): Unit =
    timer.start(
      t => subjectTimer.onNext(DurationFormatUtils.formatDuration(t.toMillis, "HH:mm:ss", true)),
      println("time is up!")
    )
    timer.changeTickPeriod(tickPeriod)

  before {
    areaComponentsState = AreaComponentsState()
    humiditySensor = SoilHumiditySensor(
      areaComponentsState,
      (callback: String => Unit) =>
        subjectTimer.subscribe(
          (s: String) => {
            callback(s)
            Continue
          },
          _.printStackTrace(),
          () => {}
        )
    )
    humiditySensor.setObserverEnvironmentValue(subjectEnvironment)
    humiditySensor.setObserverActionsArea(subjectActions)
  }

  after {
    timer.stop()
  }

  test("Temperature sensor should be initialized with the initial humidity value") {
    setupTimer(1 second)
    humiditySensor.getCurrentValue shouldEqual areaComponentsState.soilHumidity
  }

  test("The soil humidity value should decrease over time due to water evaporation") {
    setupTimer(50 microseconds)
    eventually(timeout(Span(8000, Milliseconds))) {
      humiditySensor.getCurrentValue should be < areaComponentsState.soilHumidity
    }
  }

  test("The soil humidity value should increase if the area is open and is raining") {
    setupTimer(50 microseconds)
    areaComponentsState.gatesState = AreaGatesState.Open
    val precipitation = 2.0
    subjectEnvironment.onNext(precipitation)
    eventually(timeout(Span(1000, Milliseconds))) {
      humiditySensor.getCurrentValue should be > areaComponentsState.soilHumidity
    }
  }

  test("The soil humidity value should increase after watering") {
    setupTimer(1 second)
    areaComponentsState.humidityActions = AreaHumidityState.Watering
    subjectActions.onNext(areaComponentsState)
    eventually(timeout(Span(2000, Milliseconds))) {
      humiditySensor.getCurrentValue should be > areaComponentsState.soilHumidity
    }
    areaComponentsState.humidityActions shouldBe AreaHumidityState.None
  }

  test("The soil humidity value should decrease after moving the soil") {
    setupTimer(1 second)
    areaComponentsState.humidityActions = AreaHumidityState.MovingSoil
    subjectActions.onNext(areaComponentsState)
    eventually(timeout(Span(2000, Milliseconds))) {
      humiditySensor.getCurrentValue should be < areaComponentsState.soilHumidity
    }
    areaComponentsState.humidityActions shouldBe AreaHumidityState.None
  }
