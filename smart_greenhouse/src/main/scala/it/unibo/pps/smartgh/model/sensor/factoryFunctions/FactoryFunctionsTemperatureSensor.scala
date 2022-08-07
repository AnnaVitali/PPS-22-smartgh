package it.unibo.pps.smartgh.model.sensor.factoryFunctions

object FactoryFunctionsTemperatureSensor:
  private val areaFactor = 0.90
  private val environmentFactor = 0.10

  val computeTemperature: (currentAreaVal: Double, temperatureToReach: Double) => Double =
    (areaVal, tempToReach) => (areaVal * areaFactor) + (tempToReach * environmentFactor)
