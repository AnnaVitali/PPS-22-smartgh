package it.unibo.pps.smartgh.model.sensor

import monix.execution.Ack
import scala.concurrent.Future

object Sensor:
  trait Sensor:
    def initialize(area: Any): Unit //registrazione callback sull'area quando viene prodotto un nuovo valore
    def registerValueCallback(onNext: Double => Future[Ack], onError: Throwable => Unit, onComplete: () => Unit): Unit
