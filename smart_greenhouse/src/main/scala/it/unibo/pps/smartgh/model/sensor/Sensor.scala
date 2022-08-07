package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import monix.execution.Ack
import monix.reactive.{MulticastStrategy, Observable}
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState
import it.unibo.pps.smartgh.model.time.Timer
import monix.execution.Scheduler.Implicits.global
import monix.execution.Ack.Continue
import monix.reactive.subjects.ConcurrentSubject

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

trait Sensor:
  def setObserverEnvironmentValue(observableEnvironment: Observable[Double]): Unit =
    observableEnvironment.subscribe(onNextEnvironmentValue(), (ex: Throwable) => ex.printStackTrace(), () => {})

  def setObserverActionsArea(
      observableActionArea: Observable[AreaComponentsStateImpl]
  ): Unit =
    observableActionArea.subscribe(onNextAction(), (ex: Throwable) => ex.printStackTrace(), () => {})

  def registerValueCallback(onNext: Double => Future[Ack], onError: Throwable => Unit, onComplete: () => Unit): Unit

  def onNextAction(): AreaComponentsStateImpl => Future[Ack]

  def onNextEnvironmentValue(): Double => Future[Ack]

  def getCurrentValue(): Double

trait SensorWithTimer extends Sensor:

  def registerTimerCallback(): Unit
  def onNextTimerEvent(): FiniteDuration => Unit

abstract class AbstractSensor(var areaComponentsState: AreaComponentsStateImpl) extends Sensor:

  protected var currentEnvironmentValue: Double = _
  protected val subject: ConcurrentSubject[Double, Double] = ConcurrentSubject[Double](MulticastStrategy.publish)
  protected var currentValue: Double = _

  override def getCurrentValue(): Double = currentValue

  protected def computeNextSensorValue(): Unit

  override def registerValueCallback(
      onNext: Double => Future[Ack],
      onError: Throwable => Unit,
      onComplete: () => Unit
  ): Unit = subject.subscribe(onNext, onError, onComplete)

  override def onNextEnvironmentValue(): (newEnvironmentValue: Double) => Future[Ack] = envVal =>
    currentEnvironmentValue = envVal
    computeNextSensorValue()
    Continue

  override def onNextAction(): AreaComponentsStateImpl => Future[Ack] = currentAreaComponentsState =>
    areaComponentsState = currentAreaComponentsState
    computeNextSensorValue()
    Continue

abstract class AbstractSensorWithTimer(areaComponentsState: AreaComponentsStateImpl, timer: Timer)
    extends AbstractSensor(areaComponentsState)
    with SensorWithTimer:

  override def onNextTimerEvent(): FiniteDuration => Unit = time => computeNextSensorValue()
