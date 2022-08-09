package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaAtomiseState, AreaGatesState, AreaVentilationState}
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsAirHumidity
import it.unibo.pps.smartgh.model.time.Timer
import monix.execution.Ack
import monix.execution.Ack.Continue
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject

import scala.concurrent.Future
import monix.execution.Scheduler.Implicits.global

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
      timer: Timer
  ): AirHumiditySensorImpl =
    AirHumiditySensorImpl(areaComponentsState, timer)

  /** Class that represents the air humidity sensor of an area of the greenhouse.
    * @param areaComponentsState
    *   the actual state of the area components.
    * @param timer
    *   the timer of the simulation
    */
  class AirHumiditySensorImpl(areaComponentsState: AreaComponentsStateImpl, timer: Timer)
      extends AbstractSensorWithTimer(areaComponentsState, timer):

    private val timeMustPass: Int = 3600
    private val minPercentage = 0.01
    private val maxPercentage = 0.05
    private var maxAtomizeValue: Double = _
    private var minVentilateValue: Double = _
    private val noActionRandomVal: Double = areaComponentsState.gatesState match
      case AreaGatesState.Close => calculateRandomValue(currentValue)
      case _ => 0.0

    currentValue = areaComponentsState.airHumidity - calculateRandomValue(currentValue)

    private def calculateRandomValue: Double => Double = value =>
      value * Random().nextDouble() * maxPercentage + minPercentage

    override def registerTimerCallback(): Unit =
      timer.addCallback(onNextTimerEvent(), timeMustPass)

    override def computeNextSensorValue(): Unit =
      areaComponentsState.gatesState match
        case AreaGatesState.Open =>
          currentValue = currentEnvironmentValue
        case _ =>
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

    override def onNextAction(): AreaComponentsStateImpl => Future[Ack] =
      maxAtomizeValue = currentValue + calculateRandomValue(currentValue)
      minVentilateValue = currentValue - calculateRandomValue(currentValue)
      super.onNextAction()
