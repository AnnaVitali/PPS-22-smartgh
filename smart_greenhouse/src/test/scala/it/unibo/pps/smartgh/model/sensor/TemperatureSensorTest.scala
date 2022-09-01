package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaComponentsState
import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaShieldState}
import it.unibo.pps.smartgh.model.sensor.TemperatureSensor.TemperatureSensorImpl
import it.unibo.pps.smartgh.model.time.Timer
import monix.execution.Ack.Continue
import org.scalatest.funsuite.AnyFunSuite
import monix.execution.Scheduler.Implicits.global
import org.scalatest.matchers.should.Matchers
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.MulticastStrategy
import org.apache.commons.lang3.time.DurationFormatUtils
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Milliseconds, Span}

import scala.concurrent.duration.*
import scala.language.postfixOps

/** This class contains the tests realized to verify that [[TemperatureSensor]] behaves correctly. */
class TemperatureSensorTest extends AnyFunSuite with Matchers with BeforeAndAfter with Eventually:

  private val TS = "Temperature sensor"
  private val areaComponentsState = AreaComponentsState()
  private val timer = Timer(1 day)
  private var temperatureSensor: TemperatureSensorImpl = _
  private val subjectEnvironment: ConcurrentSubject[Double, Double] =
    ConcurrentSubject[Double](MulticastStrategy.publish)
  private val subjectActions: ConcurrentSubject[AreaComponentsStateImpl, AreaComponentsStateImpl] =
    ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)
  private val subjectTimer = ConcurrentSubject[String](MulticastStrategy.publish)

  before {
    temperatureSensor = TemperatureSensor(
      areaComponentsState,
      (callback: String => Unit) =>
        subjectTimer.subscribe(
          (s: String) => {
            callback(s)
            Continue
          },
          (ex: Throwable) => ex.printStackTrace(),
          () => {}
        )
    )
    temperatureSensor.setObserverEnvironmentValue(subjectEnvironment)
    temperatureSensor.setObserverActionsArea(subjectActions)
    timer.start(
      t => subjectTimer.onNext(DurationFormatUtils.formatDuration(t.toMillis, "HH:mm:ss", true)),
      println("time is up!")
    )
    timer.changeTickPeriod(10 milliseconds)
  }

  after {
    timer.stop()
  }

  test(s"$TS must be initialized with the internal environment value") {
    temperatureSensor.getCurrentValue shouldEqual areaComponentsState.temperature
  }

  test(
    s"the current temperature value when the area is open and a new environment value has been emitted " +
      s"should approach this new environment value"
  ) {
    val environmentValue = 37.0
    var firstApproach: Double = 0.0

    subjectEnvironment.onNext(environmentValue)

    eventually(timeout(Span(5000, Milliseconds))) {
      firstApproach = temperatureSensor.getCurrentValue
      firstApproach should be <= environmentValue
    }

    areaComponentsState.gatesState = AreaGatesState.Open
    subjectActions.onNext(areaComponentsState)

    eventually(timeout(Span(5000, Milliseconds))) {
      temperatureSensor.getCurrentValue should (be >= firstApproach and be <= environmentValue)
    }
  }

  test(
    s"the current temperature value when the area is close and a new environment value has been emitted " +
      s"should approach the internal temperature"
  ) {
    val environmentValue = 37.0
    var firstApproach: Double = 0.0
    areaComponentsState.gatesState = AreaGatesState.Close
    subjectActions.onNext(areaComponentsState)

    eventually(timeout(Span(5000, Milliseconds))) {
      firstApproach = temperatureSensor.getCurrentValue
      firstApproach should be <= environmentValue
    }

    areaComponentsState.temperature = 27.0
    subjectEnvironment.onNext(environmentValue)

    eventually(timeout(Span(5000, Milliseconds))) {
      temperatureSensor.getCurrentValue should (be <= firstApproach and be >= areaComponentsState.temperature and be <= environmentValue)
    }
  }
