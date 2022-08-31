package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.AreaGatesState.{Close, Open}
import it.unibo.pps.smartgh.model.area.AreaHumidityState.{MovingSoil, None, Watering}
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsSoilHumidity
import monix.eval.Task
import monix.execution.Ack
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future
import scala.concurrent.duration.*

/** Object that enclose the implementation of the soil humidity sensor. */
object SoilHumiditySensor:

  private val TimeMustPass = "00:00"
  private val ValueRange = (0.0, 100.0)

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
      override val areaComponentsState: AreaComponentsStateImpl,
      private val addTimerCallback: (f: String => Unit) => Unit
  ) extends AbstractSensorWithTimer(areaComponentsState, addTimerCallback):

    private val checkInRange: Double => Double = _.max(ValueRange._1).min(ValueRange._2)

    currentValue = areaComponentsState.soilHumidity
    registerTimerCallback(_.takeRight(5).contentEquals(TimeMustPass))

    override def computeNextSensorValue(): Unit =
      import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsSoilHumidity.*
      Task {
        currentValue = checkInRange(areaComponentsState.humidityActions match
          case Watering => updateWateringValue(currentValue)
          case MovingSoil => updateMovingSoilValue(currentValue)
          case _ =>
            areaComponentsState.gatesState match
              case Open if currentEnvironmentValue > 0.0 => updateGatesOpenValue(currentValue, currentEnvironmentValue)
              case _ => updateEvaporationValue(currentValue)
        )
        areaComponentsState.humidityActions = None
        subject.onNext(currentValue)
      }.executeAsync.runToFuture
