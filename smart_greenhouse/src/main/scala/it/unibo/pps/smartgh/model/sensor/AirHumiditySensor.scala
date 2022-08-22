package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.{AreaAtomiseState, AreaGatesState, AreaVentilationState}
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsAirHumidity
import it.unibo.pps.smartgh.model.time.Timer
import monix.eval.Task
import monix.execution.Ack
import monix.execution.Ack.Continue
import monix.execution.Scheduler.Implicits.global
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject

import scala.concurrent.Future
import scala.util.Random

/** Object that enclose the implementation of the air humidity sensor. */
object AirHumiditySensor:

  /** Apply method for the [[AirHumiditySensorImpl]]
    * @param areaComponentsState
    *   the actual state of the area components.
    * @param timer
    *   the timer of the simulation
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
      areaComponentsState: AreaComponentsStateImpl,
      addTimerCallback: (f: String => Unit) => Unit
  ) extends AbstractSensorWithTimer(areaComponentsState, addTimerCallback):

    private val valueRange = (0.0, 100.0)
    private val timeMustPass: Int = 3600
    private val minPercentage = 0.01
    private val maxPercentage = 0.02
    private val initialHumidity = 80.0
    private var maxAtomizeValue: Double = _
    private var minVentilateValue: Double = _
    private val noActionRandomVal: Double = areaComponentsState.gatesState match
      case AreaGatesState.Close => calculateRandomValue(currentValue)
      case _ => 0.0

    currentValue = initialHumidity

    private def calculateRandomValue: Double => Double = value =>
      value * Random().nextDouble() * maxPercentage + minPercentage

    override def registerTimerCallback(): Unit =
      addTimerCallback((s: String) => if s.takeRight(5).contentEquals("00:00") then onNextTimerEvent())

    override def computeNextSensorValue(): Unit =
      Task {
        areaComponentsState.gatesState match
          case AreaGatesState.Open => currentValue = currentEnvironmentValue
          case _ => currentValue = (currentEnvironmentValue - calculateRandomValue(currentValue)).max(valueRange._1)
        (areaComponentsState.atomisingState, areaComponentsState.ventilationState) match
          case (AreaAtomiseState.AtomisingActive, AreaVentilationState.VentilationInactive) =>
            currentValue = FactoryFunctionsAirHumidity.updateAtomizeValue(currentValue, maxAtomizeValue)
          case (AreaAtomiseState.AtomisingInactive, AreaVentilationState.VentilationActive) =>
            currentValue = FactoryFunctionsAirHumidity.updateVentilationValue(currentValue, minVentilateValue)
          case (AreaAtomiseState.AtomisingInactive, AreaVentilationState.VentilationInactive) =>
            currentValue = FactoryFunctionsAirHumidity.updateDisableActionValue(
              currentValue,
              currentEnvironmentValue,
              noActionRandomVal
            )
          case _ =>
        subject.onNext(currentValue)
      }.executeAsync.runToFuture

    override def onNextAction(): AreaComponentsStateImpl => Future[Ack] =
      maxAtomizeValue = (currentValue + calculateRandomValue(currentValue)).min(valueRange._2)
      minVentilateValue = (currentValue - calculateRandomValue(currentValue)).max(valueRange._1)
      super.onNextAction()
