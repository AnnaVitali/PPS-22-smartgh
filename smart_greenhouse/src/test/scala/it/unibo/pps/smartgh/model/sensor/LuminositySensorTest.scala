package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.{AreaComponentsState, AreaGatesState, AreaShieldState}
import it.unibo.pps.smartgh.model.sensor.LuminositySensor.LuminositySensorImpl
import monix.execution.Scheduler.Implicits.global
import monix.reactive.MulticastStrategy
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.subjects.ConcurrentSubject
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Milliseconds, Span}

import scala.util.Random

/** This class contains the tests realized to verify that [[LuminositySensor]] behaves correctly. */
class LuminositySensorTest extends AnyFunSuite with Matchers with BeforeAndAfter with Eventually:

  private val initialLuminosity = 500.0
  private val minPercentage = 0.01
  private val maxPercentage = 0.05
  private var areaComponentsState: AreaComponentsStateImpl = _
  private var luminositySensor: LuminositySensorImpl = _
  private val subjectEnvironment: ConcurrentSubject[Double, Double] =
    ConcurrentSubject[Double](MulticastStrategy.publish)
  private val subjectActions: ConcurrentSubject[AreaComponentsStateImpl, AreaComponentsStateImpl] =
    ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)

  before {
    areaComponentsState = AreaComponentsState()
    luminositySensor = LuminositySensor(initialLuminosity, areaComponentsState)
    luminositySensor.setObserverEnvironmentValue(subjectEnvironment)
    luminositySensor.setObserverActionsArea(subjectActions)
  }

  test(s"Luminosity sensor must be initialized with a lower value then that of the environment") {
    luminositySensor.getCurrentValue should be < initialLuminosity
  }

  test(
    s"the current luminosity value when no action has been performed but a new environment value has been " +
      s"emitted should be the environment value plus the lamp brightness value"
  ) {
    val environmentValue = 30000.0
    subjectEnvironment.onNext(environmentValue)

    eventually(timeout(Span(5000, Milliseconds))) {
      luminositySensor.getCurrentValue should be(environmentValue + areaComponentsState.brightnessOfTheLamps)
    }
  }

  test(
    s"the current luminosity value when the area's shields are up and its gates are opened should be:" +
      s" environmentValue + lampBrightness"
  ) {
    val environmentValue = 30000.0
    areaComponentsState.shieldState = AreaShieldState.Up
    areaComponentsState.gatesState = AreaGatesState.Open
    subjectEnvironment.onNext(environmentValue)
    subjectActions.onNext(areaComponentsState)

    eventually(timeout(Span(5000, Milliseconds))) {
      luminositySensor.getCurrentValue shouldEqual (environmentValue + areaComponentsState.brightnessOfTheLamps)
    }
  }

  test(
    s"the current luminosity value when the area's shields are up and its gates are closed should be" +
      s"a value between the maximum possible and the minimum"
  ) {
    val environmentValue = 30000.0
    val maxValue = environmentValue - (minPercentage * environmentValue) + areaComponentsState.brightnessOfTheLamps
    val minValue = environmentValue - (maxPercentage * environmentValue) + areaComponentsState.brightnessOfTheLamps
    areaComponentsState.shieldState = AreaShieldState.Up
    areaComponentsState.gatesState = AreaGatesState.Close
    subjectEnvironment.onNext(environmentValue)
    subjectActions.onNext(areaComponentsState)

    eventually(timeout(Span(5000, Milliseconds))) {
      luminositySensor.getCurrentValue should (be >= minValue and be <= maxValue)
    }
  }

  test(
    s"the current luminosity value when the area's shields are up and its gates are closed should be equals lampBrightness"
  ) {
    areaComponentsState.shieldState = AreaShieldState.Down
    areaComponentsState.gatesState = AreaGatesState.Close
    subjectActions.onNext(areaComponentsState)

    eventually(timeout(Span(3000, Milliseconds))) {
      luminositySensor.getCurrentValue shouldEqual areaComponentsState.brightnessOfTheLamps
    }
  }
