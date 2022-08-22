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

import scala.concurrent.duration.*
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
  def apply(
      areaComponentsState: AreaComponentsStateImpl,
      addTimerCallback: (f: String => Unit) => Unit
  ): SoilHumiditySensorImpl =
    SoilHumiditySensorImpl(areaComponentsState, addTimerCallback)

  /** Class that represents the soil humidity sensor of an area of the greenhouse.
    * @param areaComponentsState
    *   the actual state of the area components.
    * @param addTimerCallback
    *   the callback for the timer.
    */
  class SoilHumiditySensorImpl(
      areaComponentsState: AreaComponentsStateImpl,
      addTimerCallback: (f: String => Unit) => Unit
  ) extends AbstractSensorWithTimer(areaComponentsState, addTimerCallback):

    private val timeMustPass: Int = 3600
    currentValue = areaComponentsState.soilHumidity
    registerTimerCallback(_.takeRight(5).contentEquals("00:00"))

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
