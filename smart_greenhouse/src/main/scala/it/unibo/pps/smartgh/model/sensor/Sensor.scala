package it.unibo.pps.smartgh.model.sensor

object Sensor:
  trait Sensor:
    def updateEnvironmentValue(): Unit
    def registerValueCallback(f: Double => Unit): Unit
