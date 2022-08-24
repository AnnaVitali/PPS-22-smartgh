package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.{AreaComponentsState, AreaGatesState, AreaShieldState}
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsTemperature
import it.unibo.pps.smartgh.model.time.Timer
import monix.eval.Task
import monix.execution.Ack
import monix.execution.Ack.{Continue, Stop}
import monix.execution.Scheduler.Implicits.global
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.{MulticastStrategy, Observable}
import monix.reactive.subjects.ConcurrentSubject

import scala.concurrent.Future
import scala.util.Random

/** Object that enclose the implementation of the temperature sensor. */
object TemperatureSensor:

  /** Apply method for the [[TemperatureSensorImpl]]
    * @param areaComponentsStateImpl
    *   the actual state of the are components.
    * @param addTimerCallback
    *   the callback for the timer.
    * @return
    *   the sensor responsible for detecting the temperature of the area.
    */
  def apply(
      areaComponentsStateImpl: AreaComponentsStateImpl,
      addTimerCallback: (f: String => Unit) => Unit
  ): TemperatureSensorImpl =
    TemperatureSensorImpl(areaComponentsStateImpl, addTimerCallback)

  /** Class that represents the temperature sensor of an area of the greenhouse.
    * @param areaComponentsState
    *   represents the current state of the components of the area.
    * @param addTimerCallback
    *   the callback for the timer.
    */
  class TemperatureSensorImpl(
      areaComponentsState: AreaComponentsStateImpl,
      addTimerCallback: (f: String => Unit) => Unit
  ) extends AbstractSensorWithTimer(areaComponentsState, addTimerCallback):
    private val timeMustPass: Int = 5

    currentValue = areaComponentsState.temperature
    registerTimerCallback(_.takeRight(2).toInt % timeMustPass == 0)

    override def computeNextSensorValue(): Unit =
      Task {
        areaComponentsState.gatesState match
          case AreaGatesState.Open if currentValue != currentEnvironmentValue =>
            currentValue = FactoryFunctionsTemperature.updateTemperatureApproachingTemperatureToReach(
              currentValue,
              currentEnvironmentValue
            )
          case AreaGatesState.Close if currentValue != areaComponentsState.temperature =>
            currentValue = FactoryFunctionsTemperature.updateTemperatureApproachingTemperatureToReach(
              currentValue,
              areaComponentsState.temperature
            )
          case _ =>
        subject.onNext(currentValue)
      }.executeAsync.runToFuture
