package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import monix.execution.Ack
import monix.reactive.Observable
import monix.execution.Scheduler.Implicits.global
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.MulticastStrategy
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaGatesState, AreaHumidityState}
import monix.execution.Ack.Continue

import scala.concurrent.Future

object SoilHumiditySensor:

  object FactoryFunctionsSoilHumidity:

    private val valueRange = (0.0, 100.0)
    private val evaporationFactor = 100
    private val rainFactor = 50.0 / 100
    private val wateringFactor = 5.0 / 100
    private val movingSoilFactor = 10.0 / 100

    val updateValueWithEvaporation: Double => Double = currentAreaValue =>
      (currentAreaValue - currentAreaValue / evaporationFactor).max(valueRange._1)

    val updateValueWithAreaGatesOpen: (Double, Double) => Double = (currentAreaValue, precipitation) =>
      (currentAreaValue - currentAreaValue / evaporationFactor + precipitation * rainFactor)
        .max(valueRange._1)
        .min(valueRange._2)

    val updateValueWithWatering: Double => Double = currentAreaValue =>
      (currentAreaValue + currentAreaValue * wateringFactor).min(valueRange._2)

    val updateValueWithMovingSoil: Double => Double = currentAreaValue =>
      (currentAreaValue - currentAreaValue * movingSoilFactor).max(valueRange._1)

  class SoilHumiditySensorImpl(var areaComponentsState: AreaComponentsStateImpl) extends SensorWithTimer:

    private val subject: ConcurrentSubject[Double, Double] = ConcurrentSubject[Double](MulticastStrategy.publish)
    private var envPrecipitationVal: Double = _
    private var currentValue = 30.0

    override def registerValueCallback(
        onNext: Double => Future[Ack],
        onError: Throwable => Unit,
        onComplete: () => Unit
    ): Unit = subject.subscribe(onNext, onError, onComplete)

    override def getCurrentValue(): Double = currentValue

    private def computeNextSensorValue(): Unit =
      areaComponentsState.humidityActions() match
        case AreaHumidityState.Watering =>
          FactoryFunctionsSoilHumidity.updateValueWithWatering(currentValue)
          areaComponentsState.humidityActions = AreaHumidityState.None
        case AreaHumidityState.MovingSoil =>
          FactoryFunctionsSoilHumidity.updateValueWithMovingSoil(currentValue)
          areaComponentsState.humidityActions = AreaHumidityState.None
        case _ =>
      areaComponentsState.gatesState() match
        case AreaGatesState.Close =>
          currentValue = FactoryFunctionsSoilHumidity.updateValueWithEvaporation(currentValue)
        case _ =>
          currentValue = FactoryFunctionsSoilHumidity.updateValueWithAreaGatesOpen(currentValue, envPrecipitationVal)
      subject.onNext(currentValue)

    override def onNextEnvironmentValue(): Double => Future[Ack] = envVal =>
      envPrecipitationVal = envVal
      computeNextSensorValue()
      Continue

    override def onNextAction(): AreaComponentsStateImpl => Future[Ack] = currentAreaComponentsState =>
      areaComponentsState = currentAreaComponentsState
      computeNextSensorValue()
      Continue

    override def onNextTimerEvent(): TimerEvent => Future[Ack] = timerEvent =>
      computeNextSensorValue()
      Continue
