package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.SoilHumiditySensor.SoilHumiditySensorImpl
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaGatesState, AreaHumidityState}
import it.unibo.pps.smartgh.model.time.Timer
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject
import monix.execution.Scheduler.Implicits.global
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Milliseconds, Span}

import scala.concurrent.duration.*
import scala.language.postfixOps

class SoilHumiditySensorTest extends AnyFunSuite with Matchers with Eventually with BeforeAndAfter:

  private var areaComponentsState: AreaComponentsStateImpl = _
  private val timer = Timer(1 day)
  private val initialHumidity = 30.0
  private var humiditySensor: SoilHumiditySensorImpl = _
  private val subjectEnvironment: ConcurrentSubject[Double, Double] =
    ConcurrentSubject[Double](MulticastStrategy.publish)
  private val subjectActions: ConcurrentSubject[AreaComponentsStateImpl, AreaComponentsStateImpl] =
    ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)

  private def setupTimer(tickPeriod: FiniteDuration): Unit =
    timer.start(println("time is up!"))
    timer.changeTickPeriod(tickPeriod)
    humiditySensor.registerTimerCallback()

  before {
    areaComponentsState = AreaComponentsState()
    humiditySensor = SoilHumiditySensor(initialHumidity, areaComponentsState, timer)
    humiditySensor.setObserverEnvironmentValue(subjectEnvironment)
    humiditySensor.setObserverActionsArea(subjectActions)
  }

  after {
    timer.stop()
  }

  test("Temperature sensor should be initialized with the initial humidity value") {
    humiditySensor.getCurrentValue() shouldEqual initialHumidity
  }

  test("The soil humidity value should decrease over time due to water evaporation") {
    setupTimer(50 microseconds)
    eventually(timeout(Span(5000, Milliseconds))) {
      humiditySensor.getCurrentValue() should be < initialHumidity
    }
  }

  test("The soil humidity value should increase if the area is open and is raining") {
    setupTimer(50 microseconds)
    areaComponentsState.gatesState = AreaGatesState.Open
    val precipitation = 2.0
    subjectEnvironment.onNext(precipitation)
    eventually(timeout(Span(1000, Milliseconds))) {
      humiditySensor.getCurrentValue() should be > initialHumidity
    }
  }

  test("The soil humidity value should increase after watering") {
    setupTimer(1 second)
    areaComponentsState.humidityActions = AreaHumidityState.Watering
    subjectActions.onNext(areaComponentsState)
    eventually(timeout(Span(2000, Milliseconds))) {
      humiditySensor.getCurrentValue() should be > initialHumidity
    }
    areaComponentsState.humidityActions shouldBe AreaHumidityState.None
  }

  test("The soil humidity value should decrease after moving the soil") {
    setupTimer(1 second)
    areaComponentsState.humidityActions = AreaHumidityState.MovingSoil
    subjectActions.onNext(areaComponentsState)
    eventually(timeout(Span(2000, Milliseconds))) {
      humiditySensor.getCurrentValue() should be < initialHumidity
    }
    areaComponentsState.humidityActions shouldBe AreaHumidityState.None
  }
