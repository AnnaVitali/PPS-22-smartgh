package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaAtomiseState.{AtomisingActive, AtomisingInactive}
import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.AreaGatesState.{Close, Open}
import it.unibo.pps.smartgh.model.area.AreaVentilationState.{VentilationActive, VentilationInactive}
import it.unibo.pps.smartgh.model.time.Timer
import monix.eval.Task
import monix.execution.Ack
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future
import scala.language.postfixOps
import scala.util.Random

/** Object that enclose the implementation of the air humidity sensor. */
object AirHumiditySensor:

  private val ValueRange = (0.0, 100.0)
  private val MinPercent = 0.10
  private val MaxPercent = 0.20
  private val CloseGatesActionFactor = 2.0
  private val OpenGatesActionFactor = 1.0
  private val InitialHumidity = 80.0
  private val TimeMustPass = "0:00"

  /** Apply method for the [[AirHumiditySensorImpl]]
    * @param areaComponentsState
    *   the actual state of the area components.
    * @param addTimerCallback
    *   the callback for the timer.
    * @return
    *   the sensor responsible for detecting the air humidity of the area.
    */
  def apply(
      areaComponentsState: AreaComponentsStateImpl,
      addTimerCallback: (f: String => Unit) => Unit
  ): AirHumiditySensorImpl =
    AirHumiditySensorImpl(areaComponentsState, addTimerCallback)

  /** Class that represents the air humidity sensor of an area of the greenhouse.
    * @param areaComponentsState
    *   the actual state of the area components.
    * @param addTimerCallback
    *   the callback for the timer.
    */
  class AirHumiditySensorImpl(
      override val areaComponentsState: AreaComponentsStateImpl,
      private val addTimerCallback: (f: String => Unit) => Unit
  ) extends AbstractSensorWithTimer(areaComponentsState, addTimerCallback):

    private val checkInRange: Double => Double = _.max(ValueRange._1).min(ValueRange._2)
    private val calcPercentValue: ((Double, Double) => Double) => Double = _(currentValue, currentValue * MaxPercent)
    private var actionValueRange: (Double, Double) = _

    currentValue = InitialHumidity
    registerTimerCallback(_.takeRight(4).contentEquals(TimeMustPass))

    private def actionFactor: Double = areaComponentsState.gatesState match
      case Close => CloseGatesActionFactor
      case _ => OpenGatesActionFactor

    override def computeNextSensorValue(): Unit =
      import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsAirHumidity.*
      Task {
        currentValue = checkInRange((areaComponentsState.atomisingState, areaComponentsState.ventilationState) match
          case (AtomisingActive, _) => updateAtomizeValue(currentValue, actionFactor, actionValueRange._2)
          case (_, VentilationActive) => updateVentilationValue(currentValue, actionFactor, actionValueRange._1)
          case (AtomisingInactive, VentilationInactive) =>
            actionValueRange = (calcPercentValue(_ - _), calcPercentValue(_ + _))
            areaComponentsState.gatesState match
              case Open => updateNoActionValue(currentValue, currentEnvironmentValue, 0.0)
              case _ => updateNoActionValue(currentValue, currentEnvironmentValue, calcPercentValue(_ - _) * MinPercent)
        )
        subject.onNext(currentValue)
      }.executeAsync.runToFuture
