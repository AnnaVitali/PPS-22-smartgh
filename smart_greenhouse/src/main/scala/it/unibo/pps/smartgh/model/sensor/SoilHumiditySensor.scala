package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.AreaGatesState
import it.unibo.pps.smartgh.model.area.AreaHumidityState.{MovingSoil, None, Watering}
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsSoilHumidity
import monix.eval.Task
import monix.execution.Ack
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future
import scala.concurrent.duration.*

/** Object that enclose the implementation of the soil humidity sensor. */
object SoilHumiditySensor:

  /** Apply method for the [[SoilHumiditySensorImpl]].
    * @param areaComponentsState
    *   the actual state of the area components.
    * @param addTimerCallback
    *   the callback for the timer.
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

    currentValue = areaComponentsState.soilHumidity
    registerTimerCallback(_.takeRight(5).contentEquals("00:00"))

    override def computeNextSensorValue(): Unit =
      import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsSoilHumidity.*
      Task {
        currentValue = areaComponentsState.humidityActions match
          case Watering =>
            areaComponentsState.humidityActions = None
            updateValueWithWatering(currentValue)
          case MovingSoil =>
            areaComponentsState.humidityActions = None
            updateValueWithMovingSoil(currentValue)
          case _ =>
            areaComponentsState.gatesState match
              case AreaGatesState.Close => updateValueWithEvaporation(currentValue)
              case _ => updateValueWithAreaGatesOpen(currentValue, currentEnvironmentValue)
        subject.onNext(currentValue)
      }.executeAsync.runToFuture
