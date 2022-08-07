package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaGatesState, AreaShieldState}
import org.scalatest.funsuite.AnyFunSuite
import monix.execution.Scheduler.Implicits.global
import org.scalatest.matchers.should.Matchers
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.MulticastStrategy

import scala.util.Random

class LuminositySensorTest extends AnyFunSuite with Matchers:

  private val LS = "Luminosity sensor"
  private val initialLuminosity = 500.0
  private val minPercentage = 0.1
  private val maxPercentage = 0.3
  private val randomValue: Random = Random(10)
  private val areaComponentsState = AreaComponentsState()
  private val luminositySensor = LuminositySensor(initialLuminosity, areaComponentsState)
  private val subjectEnvironment: ConcurrentSubject[Double, Double] =
    ConcurrentSubject[Double](MulticastStrategy.publish)
  private val subjectActions: ConcurrentSubject[AreaComponentsStateImpl, AreaComponentsStateImpl] =
    ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)

  luminositySensor.setObserverEnvironmentValue(subjectEnvironment)
  luminositySensor.setObserverActionsArea(subjectActions)

  test(s"$LS must be initialized with a lower value then that of the environment") {
    luminositySensor.getCurrentValue() should be < initialLuminosity
  }

  test(
    s"the current luminosity value when no action has been performed but a new environment value has been emitted should be:" +
      s"environmentValue - (randomValue between [0 e 5%] * environmentValue) + defaultLampBrightness"
  ) {
    val defaultLampBrightness = 100
    val newEnvironmentValue = 30000
    subjectEnvironment.onNext(newEnvironmentValue)

    Thread.sleep(1000)

    println("first test value: " + luminositySensor.getCurrentValue())
    luminositySensor
      .getCurrentValue() shouldEqual (newEnvironmentValue - (minPercentage + (maxPercentage - minPercentage) * randomValue
      .nextDouble()) * newEnvironmentValue + defaultLampBrightness)
  }

  test(
    s"the current luminosity value when the area's shields are up and its gates are opened should be:" +
      s" environmentValue + lampBrightness"
  ) {
    val environmentValue = 30000
    areaComponentsState.shieldState = AreaShieldState.Up
    areaComponentsState.gatesState = AreaGatesState.Open
    subjectActions.onNext(areaComponentsState)

    Thread.sleep(1000)

    luminositySensor.getCurrentValue() shouldEqual (environmentValue + areaComponentsState.brightnessOfTheLamps)
  }

  test(
    s"the current luminosity value when the area's shields are up and its gates are closed should be:" +
      s" environmentValue - (randomValue between [0 e 5%] * environmentValue) + lampBrightness"
  ) {
    val environmentValue = 30000
    areaComponentsState.shieldState = AreaShieldState.Up
    areaComponentsState.gatesState = AreaGatesState.Close

    subjectActions.onNext(areaComponentsState)

    Thread.sleep(1000)

    luminositySensor
      .getCurrentValue() shouldEqual (environmentValue - (minPercentage + (maxPercentage - minPercentage) * randomValue
      .nextDouble()) * environmentValue + areaComponentsState.brightnessOfTheLamps)
  }

  test(
    s"the current luminosity value when the area's shields are up and its gates are closed should be equals lampBrightness"
  ) {
    areaComponentsState.shieldState = AreaShieldState.Down
    areaComponentsState.gatesState = AreaGatesState.Close
    subjectActions.onNext(areaComponentsState)

    Thread.sleep(1000)

    println("fourth test value: " + luminositySensor.getCurrentValue())
    luminositySensor.getCurrentValue() shouldEqual areaComponentsState.brightnessOfTheLamps
  }
