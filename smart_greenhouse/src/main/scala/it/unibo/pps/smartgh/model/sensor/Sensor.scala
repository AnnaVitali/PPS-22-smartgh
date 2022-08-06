package it.unibo.pps.smartgh.model.sensor

import monix.execution.Ack
import monix.reactive.Observable

import scala.concurrent.Future

trait Sensor:
  def initialize(
      area: Observable[Any]
  ): Unit //registrazione callback sull'area quando viene prodotto un nuovo valore o cambia lo stato
  def registerValueCallback(onNext: Double => Future[Ack], onError: Throwable => Unit, onComplete: () => Unit): Unit
