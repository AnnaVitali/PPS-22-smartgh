package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaHumidityState}
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsSoilHumidity
import it.unibo.pps.smartgh.model.time.Timer
import monix.eval.Task
import monix.execution.Ack
import monix.execution.Ack.Continue
import monix.execution.Scheduler.Implicits.global
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.{MulticastStrategy, Observable}
import monix.reactive.subjects.ConcurrentSubject

import scala.concurrent.Future

/** Object that enclose the implementation of the soil humidity sensor. */
object SoilHumiditySensor:

  /** Apply method for the [[SoilHumiditySensorImpl]].
    * @param areaComponentsState
    *   the actual state of the area components.
    * @param timer
    *   the timer of the simulation.
    * @return
    *   a new sensor responsible for detecting the soil humidity of the area.
    */
  def apply(areaComponentsState: AreaComponentsStateImpl, timer: Timer): SoilHumiditySensorImpl =
    SoilHumiditySensorImpl(areaComponentsState, timer)

  /** Class that represents the soil humidity sensor of an area of the greenhouse.
    * @param areaComponentsState
    *   the actual state of the area components.
    * @param timer
    *   the timer of the simulation
    */
  class SoilHumiditySensorImpl(areaComponentsState: AreaComponentsStateImpl, timer: Timer)
      extends AbstractSensorWithTimer(areaComponentsState, timer):

    private val timeMustPass: Int = 3600
    currentValue = areaComponentsState.soilHumidity

    override def registerTimerCallback(): Unit =
      timer.addCallback(onNextTimerEvent(), timeMustPass)

    override def computeNextSensorValue(): Unit =
      Task {
        areaComponentsState.humidityActions match
          case AreaHumidityState.Watering =>
            currentValue = FactoryFunctionsSoilHumidity.updateValueWithWatering(currentValue)
            areaComponentsState.humidityActions = AreaHumidityState.None
          case AreaHumidityState.MovingSoil =>
            currentValue = FactoryFunctionsSoilHumidity.updateValueWithMovingSoil(currentValue)
            areaComponentsState.humidityActions = AreaHumidityState.None
          case _ =>
        areaComponentsState.gatesState match
          case AreaGatesState.Close =>
            currentValue = FactoryFunctionsSoilHumidity.updateValueWithEvaporation(currentValue)
          case _ =>
            currentValue =
              FactoryFunctionsSoilHumidity.updateValueWithAreaGatesOpen(currentValue, currentEnvironmentValue)
        subject.onNext(currentValue)
      }.executeAsync.runToFuture
