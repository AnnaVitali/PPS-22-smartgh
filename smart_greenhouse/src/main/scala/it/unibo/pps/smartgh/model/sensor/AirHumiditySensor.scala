package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaAtomiseState, AreaGatesState, AreaVentilationState}
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

  object FactoryFunctionsAirHumidity:

    private val valueRange = (0.0, 100.0)
    private val actionFactor = 1 / 100
    private val areaFactor = 90 / 100
    private val envFactor = 10 / 100

    val updateVentilationValue: (Double, Double) => Double = (currentValue, minValue) =>
      (currentValue - currentValue * actionFactor).max(minValue)

    val updateAtomizeValue: (Double, Double) => Double = (currentValue, maxValue) =>
      (currentValue - currentValue * actionFactor).min(maxValue)

    val updateDisableActionValue: (Double, Double, Double) => Double = (currentValue, envValue, randomVal) =>
      (currentValue * areaFactor + envValue * envFactor - randomVal).max(valueRange._1).min(valueRange._2)

  class AirHumiditySensorImpl(initialHumidity: Double, var areaComponentsState: AreaComponentsStateImpl)
      extends SensorWithTimer:

    private val minPercentage = 0.0
    private val maxPercentage = 0.05
    private val subject: ConcurrentSubject[Double, Double] = ConcurrentSubject[Double](MulticastStrategy.publish)
    private var envAirHumidityVal: Double = _
    private var currentValue = initialHumidity
    private var maxAtomizeValue: Double = _
    private var minVentilateValue: Double = _
    private val randomValue = currentValue * Random().nextDouble() * maxPercentage
    private val disableActionRandomValue = areaComponentsState.gatesState match
      case AreaGatesState.Open => 0
      case AreaGatesState.Close => randomValue

    override def registerValueCallback(
        onNext: Double => Future[Ack],
        onError: Throwable => Unit,
        onComplete: () => Unit
    ): Unit =
      subject.subscribe(onNext, onError, onComplete)

    override def getCurrentValue(): Double = currentValue

    private def computeNextSensorValue(): Unit =
      areaComponentsState.gatesState match
        case AreaGatesState.Open =>
          currentValue = envAirHumidityVal
        case _ =>
      areaComponentsState.atomisingState match
        case AreaAtomiseState.AtomisingActive =>
          FactoryFunctionsAirHumidity.updateAtomizeValue(currentValue, maxAtomizeValue)
        case _ =>
          FactoryFunctionsAirHumidity.updateDisableActionValue(
            currentValue,
            envAirHumidityVal,
            disableActionRandomValue
          )
      areaComponentsState.ventilationState match
        case AreaVentilationState.VentilationActive =>
          currentValue = FactoryFunctionsAirHumidity.updateVentilationValue(currentValue, minVentilateValue)
        case _ =>
          FactoryFunctionsAirHumidity.updateDisableActionValue(
            currentValue,
            envAirHumidityVal,
            disableActionRandomValue
          )
      subject.onNext(currentValue)

    override def onNextEnvironmentValue(): Double => Future[Ack] = envVal =>
      envAirHumidityVal = envVal
      computeNextSensorValue()
      Continue

    override def onNextAction(): AreaComponentsStateImpl => Future[Ack] = currentAreaComponentsState =>
      areaComponentsState = currentAreaComponentsState
      maxAtomizeValue = currentValue + randomValue
      minVentilateValue = currentValue - randomValue
      Continue

    override def onNextTimerEvent(): TimerEvent => Future[Ack] = timerEvent =>
      computeNextSensorValue()
      Continue
