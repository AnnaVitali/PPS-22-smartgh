package it.unibo.pps.smartgh.model.sensor

import com.sun.javafx.webkit.theme.ContextMenuImpl
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
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

  object FactoryFunctionsLuminositySensor:
    private val minPercentage = 0.1
    private val maxPercentage = 0.3
    private val randomValue = Random(10)

    val computeLuminosityWithAreaGatesOpen: (currentValEnvironment: Double, currentLampBrightness: Int) => Double =
      (valEnv, lampBrightness) => valEnv + lampBrightness

    val computeLuminosityWithAreaGatesCloseAndUnshild: (
        currentValEnvironment: Double,
        currentLampBrightness: Int
    ) => Double = (valEnv, lampBrightness) =>
      valEnv - (minPercentage + (maxPercentage - minPercentage) * randomValue.nextDouble()) * valEnv + lampBrightness

    val computeLuminosityWithAreaGatesCloseAndShild: (concurrentLampBrightness: Int) => Double = lampBrightness =>
      lampBrightness.toDouble

  class LuminositySensorImpl(initialLuminosity: Double, var areaComponentsState: AreaComponentsStateImpl)
      extends Sensor:
    private val randomValue = Random(10)
    private val minPercentage = 0.1
    private val maxPercentage = 0.3
    private var currentEnvironmentValue: Double = _
    private val subject: ConcurrentSubject[Double, Double] = ConcurrentSubject[Double](MulticastStrategy.publish)
    private var currentValue: Double =
      initialLuminosity - (minPercentage + (maxPercentage - minPercentage) * randomValue
        .nextDouble()) * initialLuminosity

    /* override def setObserverEnvironmentValue(observableEnvironment: Observable[Double]): Unit =
      observableEnvironment.subscribe(onNextEnvironmentValue(), (ex: Throwable) => ex.printStackTrace(), () => {})

    override def setObserverActionsArea(observableActionArea: Observable[AreaComponentsStateImpl]): Unit =
      observableActionArea.subscribe(onNextAction(), (ex: Throwable) => ex.printStackTrace(), () => {})*/

    override def registerValueCallback(
        onNext: Double => Future[Ack],
        onError: Throwable => Unit,
        onComplete: () => Unit
    ): Unit = subject.subscribe(onNext, onError, onComplete)

    override def getCurrentValue(): Double =
      currentValue

    private def computeNextSensorValue(): Unit =
      areaComponentsState.gatesState match
        case AreaGatesState.Open =>
          currentValue = FactoryFunctionsLuminositySensor.computeLuminosityWithAreaGatesOpen(
            currentEnvironmentValue,
            areaComponentsState.brightnessOfTheLamps
          )
          println("area gates open and shild up")
        case AreaGatesState.Close if areaComponentsState.shieldState.equals(AreaShieldState.Down) =>
          currentValue = FactoryFunctionsLuminositySensor.computeLuminosityWithAreaGatesCloseAndShild(
            areaComponentsState.brightnessOfTheLamps
          )
          println("area gates close and shild down")
        case AreaGatesState.Close if areaComponentsState.shieldState.equals(AreaShieldState.Up) =>
          currentValue = FactoryFunctionsLuminositySensor.computeLuminosityWithAreaGatesCloseAndUnshild(
            currentEnvironmentValue,
            areaComponentsState.brightnessOfTheLamps
          )
          println("area gates close and shild up")
        case _ =>
      subject.onNext(currentValue)

    override def onNextEnvironmentValue(): (newEnvironmentValue: Double) => Future[Ack] = envVal =>
      currentEnvironmentValue = envVal
      computeNextSensorValue()
      Continue

    override def onNextAction(): AreaComponentsStateImpl => Future[Ack] = currentareaComponentsState =>
      areaComponentsState = currentareaComponentsState
      computeNextSensorValue()
      Continue
