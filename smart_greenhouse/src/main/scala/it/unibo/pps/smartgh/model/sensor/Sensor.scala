package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import monix.execution.Ack
import monix.reactive.Observable
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState
import monix.execution.Scheduler.Implicits.global
import it.unibo.pps.smartgh.model.sensor.TimerEvent

import scala.concurrent.Future

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

  def setObserverTimer(observableTimer: Observable[TimerEvent]): Unit =
    observableTimer.subscribe(onNextTimerEvent(), (ex: Throwable) => ex.printStackTrace(), () => {})

  def onNextTimerEvent(): TimerEvent => Future[Ack]
