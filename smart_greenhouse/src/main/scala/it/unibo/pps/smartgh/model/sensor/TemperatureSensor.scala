package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import monix.execution.Ack
import monix.reactive.Observable

import scala.util.Random
import monix.execution.Scheduler.Implicits.global
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.MulticastStrategy
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaGatesState, AreaShildState}
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.*
import monix.execution.Ack.{Continue, Stop}

import scala.concurrent.Future

object TemperatureSensor:

  def apply(areaComponentsStateImpl: AreaComponentsStateImpl): TemperatureSensorImpl =
    TemperatureSensorImpl(areaComponentsStateImpl)

  object FactoryFunctionsTemperatureSensor:
    private val areaFactor = 0.90
    private val environmentFactor = 0.10

    val computeTemperatureWithAreaGatesOpen: (currentAreaVal: Double, currentValEnvironment: Double) => Double =
      (areaVal, envVal) => (areaVal * areaFactor) + (envVal * environmentFactor)

    val computeTemperatureWithAreaGatesClosedAndTemperatureSet: (
        currentValArea: Double,
        settedTemperatureVal: Double
    ) => Double =
      (areaVal, settedVal) => (areaVal * areaFactor) + (settedVal * environmentFactor)

  class TemperatureSensorImpl(var areaComponentsState: AreaComponentsStateImpl) extends SensorWithTimer:
    private val randomValue = Random(10)
    private val minPercentage = 0.1
    private val maxPercentage = 0.10
    private val areaFactor = 0.90
    private val environmentFactor = 0.10
    private var currentEnvironmentValue: Double = _
    private val subject: ConcurrentSubject[Double, Double] = ConcurrentSubject[Double](MulticastStrategy.publish)
    private var currentValue: Double = areaComponentsState.temperature()

    override def registerValueCallback(
        onNext: Double => Future[Ack],
        onError: Throwable => Unit,
        onComplete: () => Unit
    ): Unit = subject.subscribe(onNext, onError, onComplete)

    override def getCurrentValue(): Double =
      currentValue

    private def computeNextSensorValue(): Unit =
      areaComponentsState.gatesState() match
        case AreaGatesState.Open if currentValue < currentEnvironmentValue || currentValue > currentEnvironmentValue =>
          currentValue =
            FactoryFunctionsTemperatureSensor.computeTemperatureWithAreaGatesOpen(currentValue, currentEnvironmentValue)
        case AreaGatesState.Close
            if currentValue < areaComponentsState.temperature() || currentValue > areaComponentsState
              .temperature() =>
          FactoryFunctionsTemperatureSensor.computeTemperatureWithAreaGatesClosedAndTemperatureSet(
            currentValue,
            areaComponentsState.temperature()
          )
        case _ =>

    override def onNextEnvironmentValue(): (newEnvironmentValue: Double) => Future[Ack] = envVal =>
      currentEnvironmentValue = envVal
      computeNextSensorValue()
      Continue

    override def onNextAction(): AreaComponentsStateImpl => Future[Ack] = currentAreaComponentsState =>
      areaComponentsState = currentAreaComponentsState
      computeNextSensorValue()
      Continue

    override def onNextTimerEvent(): TimerEvent => Future[Ack] = timerEvent =>
      computeNextSensorValue()
      Continue
