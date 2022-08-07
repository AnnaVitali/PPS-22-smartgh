package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaAtomiseState, AreaGatesState, AreaVentilationState}
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsAirHumidity
import monix.execution.Ack
import monix.execution.Ack.Continue
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject

import scala.concurrent.Future
import monix.execution.Scheduler.Implicits.global

import scala.util.Random

object AirHumiditySensor:

  def apply(initialHumidity: Double, areaComponentsState: AreaComponentsStateImpl): AirHumiditySensorImpl =
    AirHumiditySensorImpl(initialHumidity, areaComponentsState)

  class AirHumiditySensorImpl(initialHumidity: Double, areaComponentsState: AreaComponentsStateImpl)
      extends AbstractSensorWithTimer(areaComponentsState):

    currentValue = initialHumidity

    private val minPercentage = 0.0
    private val maxPercentage = 0.05
    private var maxAtomizeValue: Double = _
    private var minVentilateValue: Double = _
    private val randomValue = currentValue * Random().nextDouble() * maxPercentage
    private val disableActionRandomValue = areaComponentsState.gatesState match
      case AreaGatesState.Open => 0
      case AreaGatesState.Close => randomValue

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
