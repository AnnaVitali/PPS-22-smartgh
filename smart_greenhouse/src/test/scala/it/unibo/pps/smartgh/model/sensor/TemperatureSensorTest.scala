package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaGatesState, AreaShieldState}
import it.unibo.pps.smartgh.model.time.Timer
import org.scalatest.funsuite.AnyFunSuite
import monix.execution.Scheduler.Implicits.global
import org.scalatest.matchers.should.Matchers
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.MulticastStrategy

import scala.concurrent.duration.*
import scala.language.postfixOps

class TemperatureSensorTest extends AnyFunSuite with Matchers:

  private val TS = "Temperature sensor"
  private val minPercentage = 0.1
  private val maxPercentage = 0.10
  private val areaFactor = 0.90
  private val environmentFactor = 0.10
  private val areaComponentsState = AreaComponentsState()
  private val timer = Timer(1 day)
  areaComponentsState.temperature = 30.0
  private val temperatureSensor = TemperatureSensor(areaComponentsState, timer)
  private val subjectEnvironment: ConcurrentSubject[Double, Double] =
    ConcurrentSubject[Double](MulticastStrategy.publish)
  private val subjectActions: ConcurrentSubject[AreaComponentsStateImpl, AreaComponentsStateImpl] =
    ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)

  temperatureSensor.setObserverEnvironmentValue(subjectEnvironment)
  temperatureSensor.setObserverActionsArea(subjectActions)

  test(s"$TS must be initialized with the internal environment value") {
    temperatureSensor.getCurrentValue() shouldEqual areaComponentsState.temperature
  }

  test(
    s"the current temperature value when the area is open and a new environment value has been emitted " +
      s"should approach this new environment value"
  ) {
    val environmentValue = 37.0

    timer.start(println("time is up!"))
    timer.changeTickPeriod(10 milliseconds)
    temperatureSensor.registerTimerCallback()
    subjectEnvironment.onNext(environmentValue)
    val firstApproach = temperatureSensor.getCurrentValue()
    firstApproach should be <= environmentValue

    Thread.sleep(3000)

    areaComponentsState.gatesState = AreaGatesState.Open
    temperatureSensor.getCurrentValue() should (be >= firstApproach and be <= environmentValue)
  }
