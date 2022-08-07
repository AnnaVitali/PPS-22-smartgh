package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsSoilHumidity
import monix.execution.Ack
import monix.reactive.Observable
import monix.execution.Scheduler.Implicits.global
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.MulticastStrategy
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaGatesState, AreaHumidityState}
import it.unibo.pps.smartgh.model.time.Timer
import monix.execution.Ack.Continue

import scala.concurrent.Future

object SoilHumiditySensor:

  def apply(
      initialHumidity: Double,
      areaComponentsState: AreaComponentsStateImpl,
      timer: Timer
  ): SoilHumiditySensorImpl =
    SoilHumiditySensorImpl(initialHumidity, areaComponentsState, timer)

  class SoilHumiditySensorImpl(initialHumidity: Double, areaComponentsState: AreaComponentsStateImpl, timer: Timer)
      extends AbstractSensorWithTimer(areaComponentsState, timer):

    private val timeMustPass: Int = 3600
    currentValue = initialHumidity

    override def registerTimerCallback(): Unit =
      timer.addCallback(onNextTimerEvent(), timeMustPass)

    override def computeNextSensorValue(): Unit =
      areaComponentsState.humidityActions match
        case AreaHumidityState.Watering =>
          FactoryFunctionsSoilHumidity.updateValueWithWatering(currentValue)
          areaComponentsState.humidityActions = AreaHumidityState.None
        case AreaHumidityState.MovingSoil =>
          FactoryFunctionsSoilHumidity.updateValueWithMovingSoil(currentValue)
          areaComponentsState.humidityActions = AreaHumidityState.None
        case _ =>
      areaComponentsState.gatesState match
        case AreaGatesState.Close =>
          currentValue = FactoryFunctionsSoilHumidity.updateValueWithEvaporation(currentValue)
        case _ =>
          currentValue =
            FactoryFunctionsSoilHumidity.updateValueWithAreaGatesOpen(currentValue, currentEnvironmentValue)
      subject.onNext(currentValue)
