package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsTemperatureSensor
import it.unibo.pps.smartgh.model.time.Timer
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

/** Object that enclose the implementation of the temperature sensor. */
object TemperatureSensor:

  /** Apply method for the [[TemperatureSensorImpl]]
    * @param areaComponentsStateImpl
    *   the actual state of the are components.
    * @param timer
    *   the simulation timer.
    * @return
    *   the sensor responsible for detecting the temperature of the area.
    */
  def apply(areaComponentsStateImpl: AreaComponentsStateImpl, timer: Timer): TemperatureSensorImpl =
    TemperatureSensorImpl(areaComponentsStateImpl, timer)

  /**
    * Class that represents the temperature sensor of an area of the greenhouse.
    * @param areaComponentsState
    *   represents the current state of the components of the area.
    * @param timer
    *   the simulation timer
    */
  class TemperatureSensorImpl(areaComponentsState: AreaComponentsStateImpl, timer: Timer)
    extends AbstractSensorWithTimer(areaComponentsState, timer):
    private val timeMustPass: Int = 300

    currentValue = areaComponentsState.temperature

    override def registerTimerCallback(): Unit =
      timer.addCallback(onNextTimerEvent(), timeMustPass)

    override def computeNextSensorValue(): Unit =
      areaComponentsState.gatesState match
        case AreaGatesState.Open if currentValue != currentEnvironmentValue =>
          currentValue = FactoryFunctionsTemperatureSensor.computeTemperature(currentValue, currentEnvironmentValue)
        case AreaGatesState.Close if currentValue != areaComponentsState.temperature =>
          currentValue = FactoryFunctionsTemperatureSensor.computeTemperature(currentValue, areaComponentsState.temperature)
        case _ =>
      subject.onNext(currentValue)
