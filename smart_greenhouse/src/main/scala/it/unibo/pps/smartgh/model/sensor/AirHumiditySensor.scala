package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaAtomiseState.{AtomisingActive, AtomisingInactive}
import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.AreaGatesState
import it.unibo.pps.smartgh.model.area.AreaVentilationState.{VentilationActive, VentilationInactive}
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsAirHumidity
import it.unibo.pps.smartgh.model.time.Timer
import monix.eval.Task
import monix.execution.Ack
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future
import scala.util.Random

/** Object that enclose the implementation of the air humidity sensor. */
object AirHumiditySensor:

  private val ValueRange = (0.0, 100.0)
  private val MinPercentage = 0.10
  private val MaxPercentage = 0.20
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

    private var maxAtomizeValue: Double = _
    private var minVentilateValue: Double = _
    private val noActionRandomVal: Double = areaComponentsState.gatesState match
      case AreaGatesState.Close => calculateRandomValue(currentValue)
      case _ => 0.0

    currentValue = InitialHumidity
    registerTimerCallback(_.takeRight(4).contentEquals(TimeMustPass))

    private def calculateRandomValue: Double => Double = _ * Random().nextDouble() * MaxPercentage + MinPercentage

    override def computeNextSensorValue(): Unit =
      import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsAirHumidity.*
      Task {
        currentValue = (areaComponentsState.atomisingState, areaComponentsState.ventilationState) match
          case (AtomisingActive, _) => updateAtomizeValue(currentValue, maxAtomizeValue)
          case (_, VentilationActive) => updateVentilationValue(currentValue, minVentilateValue)
          case (AtomisingInactive, VentilationInactive) =>
            updateNoActionValue(currentValue, currentEnvironmentValue, noActionRandomVal)
          case _ =>
            areaComponentsState.gatesState match
              case AreaGatesState.Open => currentEnvironmentValue
              case _ => (currentEnvironmentValue - calculateRandomValue(currentValue)).max(ValueRange._1)
        subject.onNext(currentValue)
      }.executeAsync.runToFuture

    override def onNextAction(): AreaComponentsStateImpl => Future[Ack] =
      maxAtomizeValue = (currentValue + calculateRandomValue(currentValue)).min(ValueRange._2)
      minVentilateValue = (currentValue - calculateRandomValue(currentValue)).max(ValueRange._1)
      super.onNextAction()
