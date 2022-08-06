package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import monix.execution.Ack
import monix.reactive.Observable
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState

import scala.concurrent.Future

trait Sensor:
  def setObserverEnvironmentValue(observableEnvironment: Observable[Double]): Unit

  def setObserverActionsArea(
      observableActionArea: Observable[AreaComponentsStateImpl]
  ): Unit //registrazione callback sull'area quando viene prodotto un nuovo valore o cambia lo stato
  def registerValueCallback(onNext: Double => Future[Ack], onError: Throwable => Unit, onComplete: () => Unit): Unit
