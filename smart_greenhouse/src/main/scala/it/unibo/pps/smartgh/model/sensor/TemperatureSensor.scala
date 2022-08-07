package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsTemperatureSensor
import monix.execution.Ack
import monix.reactive.Observable

import scala.util.Random
import monix.execution.Scheduler.Implicits.global
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.MulticastStrategy
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaGatesState, AreaShieldState}
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.*
import monix.execution.Ack.{Continue, Stop}

import scala.concurrent.Future

object TemperatureSensor:

  def apply(areaComponentsStateImpl: AreaComponentsStateImpl): TemperatureSensorImpl =
    TemperatureSensorImpl(areaComponentsStateImpl)

  class TemperatureSensorImpl(areaComponentsState: AreaComponentsStateImpl)
      extends AbstractSensorWithTimer(areaComponentsState):
    private val randomValue = Random(10)
    private val minPercentage = 0.1
    private val maxPercentage = 0.10
    private val areaFactor = 0.90
    private val environmentFactor = 0.10
    currentValue = areaComponentsState.temperature

    override def computeNextSensorValue(): Unit =
      areaComponentsState.gatesState match
        case AreaGatesState.Open if currentValue < currentEnvironmentValue || currentValue > currentEnvironmentValue =>
          currentValue = FactoryFunctionsTemperatureSensor.computeTemperature(currentValue, currentEnvironmentValue)
        case AreaGatesState.Close
            if currentValue < areaComponentsState.temperature || currentValue > areaComponentsState.temperature =>
          FactoryFunctionsTemperatureSensor.computeTemperature(currentValue, areaComponentsState.temperature)
        case _ =>
