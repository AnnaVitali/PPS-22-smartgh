package it.unibo.pps.smartgh.model.sensor

import com.sun.javafx.webkit.theme.ContextMenuImpl
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsLuminosity
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.AbstractSensor
import monix.execution.Ack
import monix.reactive.Observable

import scala.util.Random
import monix.execution.Scheduler.Implicits.global
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.MulticastStrategy
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.{AreaGatesState, AreaShieldState}
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.*
import monix.execution.Ack.Continue

import scala.concurrent.Future

object LuminositySensor:

  def apply(initialLuminosity: Double, areaComponentsState: AreaComponentsStateImpl): LuminositySensorImpl =
    LuminositySensorImpl(initialLuminosity, areaComponentsState)

  class LuminositySensorImpl(initialLuminosity: Double, areaComponentsState: AreaComponentsStateImpl)
      extends AbstractSensor(areaComponentsState):
    private val randomValue = Random(10)
    private val minPercentage = 0.1
    private val maxPercentage = 0.3
    currentValue = initialLuminosity - (minPercentage + (maxPercentage - minPercentage) * randomValue
      .nextDouble()) * initialLuminosity

    /* override def setObserverEnvironmentValue(observableEnvironment: Observable[Double]): Unit =
      observableEnvironment.subscribe(onNextEnvironmentValue(), (ex: Throwable) => ex.printStackTrace(), () => {})

    override def setObserverActionsArea(observableActionArea: Observable[AreaComponentsStateImpl]): Unit =
      observableActionArea.subscribe(onNextAction(), (ex: Throwable) => ex.printStackTrace(), () => {})*/

    override def computeNextSensorValue(): Unit =
      areaComponentsState.gatesState match
        case AreaGatesState.Open =>
          currentValue = FactoryFunctionsLuminosity.computeLuminosityWithAreaGatesOpen(
            currentEnvironmentValue,
            areaComponentsState.brightnessOfTheLamps
          )
          println("area gates open and shield up")
        case AreaGatesState.Close if areaComponentsState.shieldState.equals(AreaShieldState.Down) =>
          currentValue = FactoryFunctionsLuminosity.computeLuminosityWithAreaGatesCloseAndShielded(
            areaComponentsState.brightnessOfTheLamps
          )
          println("area gates close and shield down")
        case AreaGatesState.Close if areaComponentsState.shieldState.equals(AreaShieldState.Up) =>
          currentValue = FactoryFunctionsLuminosity.computeLuminosityWithAreaGatesCloseAndUnshielded(
            currentEnvironmentValue,
            areaComponentsState.brightnessOfTheLamps
          )
          println("area gates close and shield up")
        case _ =>
      subject.onNext(currentValue)
