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

object AirHumiditySensor:

  def apply(
      initialHumidity: Double,
      areaComponentsState: AreaComponentsStateImpl,
      timer: Timer
  ): AirHumiditySensorImpl =
    AirHumiditySensorImpl(initialHumidity, areaComponentsState, timer)

  class AirHumiditySensorImpl(initialHumidity: Double, areaComponentsState: AreaComponentsStateImpl, timer: Timer)
      extends AbstractSensorWithTimer(areaComponentsState, timer):

    private val timeMustPass: Int = 300

    private val minPercentage = 0.0
    private val maxPercentage = 0.05
    private var maxAtomizeValue: Double = _
    private var minVentilateValue: Double = _
    private val randomValue = currentValue * Random().nextDouble() * maxPercentage
    private val disableActionRandomValue = areaComponentsState.gatesState match
      case AreaGatesState.Open => 0
      case AreaGatesState.Close => randomValue

    currentValue = initialHumidity

    override def registerTimerCallback(): Unit =
      timer.addCallback(onNextTimerEvent(), timeMustPass)

    override def computeNextSensorValue(): Unit =
      areaComponentsState.gatesState match
        case AreaGatesState.Open =>
          currentValue = currentEnvironmentValue
        case _ =>
      areaComponentsState.atomisingState match
        case AreaAtomiseState.AtomisingActive =>
          FactoryFunctionsAirHumidity.updateAtomizeValue(currentValue, maxAtomizeValue)
        case _ =>
          FactoryFunctionsAirHumidity.updateDisableActionValue(
            currentValue,
            currentEnvironmentValue,
            disableActionRandomValue
          )
      areaComponentsState.ventilationState match
        case AreaVentilationState.VentilationActive =>
          currentValue = FactoryFunctionsAirHumidity.updateVentilationValue(currentValue, minVentilateValue)
        case _ =>
          FactoryFunctionsAirHumidity.updateDisableActionValue(
            currentValue,
            currentEnvironmentValue,
            disableActionRandomValue
          )
      subject.onNext(currentValue)

    override def onNextAction(): AreaComponentsStateImpl => Future[Ack] =
      maxAtomizeValue = currentValue + randomValue
      minVentilateValue = currentValue - randomValue
      super.onNextAction()
