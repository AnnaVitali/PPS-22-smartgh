package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.LuminositySensor.FactoryFunctionsLuminositySensor.{
  maxPercentage,
  minPercentage,
  randomValue
}
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaGatesState, AreaShildState}
import org.scalatest.funsuite.AnyFunSuite
import monix.execution.Scheduler.Implicits.global
import org.scalatest.matchers.should.Matchers
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.MulticastStrategy
import org.scalatest.BeforeAndAfter

import scala.util.Random

class LuminositySensorTest extends AnyFunSuite with Matchers with BeforeAndAfter:

  private val LS = "Luminosity sensor"
  private val initialLuminosity = 500.0
  private val minPercentage = 0.1
  private val maxPercentage = 0.5
  private var randomValue: Random = _
  private val areaComponentsState = AreaComponentsState()
  private val luminositySensor = LuminositySensor(initialLuminosity, areaComponentsState)
  private val subjectEnvironment: ConcurrentSubject[Double, Double] =
    ConcurrentSubject[Double](MulticastStrategy.publish)
  private val subjectActions: ConcurrentSubject[AreaComponentsStateImpl, AreaComponentsStateImpl] =
    ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)

  before {
    randomValue = Random(10)
    luminositySensor.setObserverEnvironmentValue(subjectEnvironment)
    luminositySensor.setObserverActionsArea(subjectActions)
  }

  test(s"$LS must be initialized with a lower value then that of the environment") {
    luminositySensor.getCurrentValue should be < initialLuminosity
  }

  test(
    s"the current luminosity value when no action has been performed but a new environment value has been emitted should be:" +
      s"environmentValue - (randomValue between [0 e 5%] * environmentValue) + defaultLampbrightness"
  ) {
    val defaultLampBrightness = 100
    val newEnvironmentValue = 30000
    subjectEnvironment.onNext(newEnvironmentValue)

    Thread.sleep(1000)

    println("first test value: " + luminositySensor.getCurrentValue)
    luminositySensor.getCurrentValue shouldEqual (newEnvironmentValue - (minPercentage + (maxPercentage - minPercentage) * Random(
      10
    )
      .nextDouble()) * newEnvironmentValue + defaultLampBrightness)
  }

  test(
    s"the current luminosity value when the area isn't shilded and it's open should be:" +
      s" environmentValue + lampBrightness"
  ) {
    val environmentValue = 30000
    areaComponentsState.gatesState = AreaGatesState.Open
    areaComponentsState.shildState = AreaShildState.Up
    subjectActions.onNext(areaComponentsState)

    Thread.sleep(1000)

    println("second test value: " + luminositySensor.getCurrentValue)
    luminositySensor.getCurrentValue shouldEqual (environmentValue + areaComponentsState.brightnessOfTheLamps())
  }

  test(
    s"the current luminosity value when the area isn't shilded and it's closed should be:" +
      s" environmentValue - (randomValue between [0 e 5%] * environmentValue) + lampBrightness"
  ) {
    val environmentValue = 30000
    areaComponentsState.gatesState = AreaGatesState.Close
    areaComponentsState.shildState = AreaShildState.Up
    subjectActions.onNext(areaComponentsState)

    Thread.sleep(1000)

    println("third test value: " + luminositySensor.getCurrentValue)
    luminositySensor.getCurrentValue shouldEqual (environmentValue - (minPercentage + (maxPercentage - minPercentage) * Random(
      10
    )
      .nextDouble()) * environmentValue + areaComponentsState.brightnessOfTheLamps())
  }

  test(
    s"the current luminosity value when the area is shilded and it's closed should be equals lampBrightness"
  ) {
    areaComponentsState.shildState = AreaShildState.Down
    areaComponentsState.gatesState = AreaGatesState.Close
    subjectActions.onNext(areaComponentsState)

    Thread.sleep(1000)

    println("fourth test value: " + luminositySensor.getCurrentValue)
    luminositySensor.getCurrentValue shouldEqual areaComponentsState.brightnessOfTheLamps()
  }
