package it.unibo.pps.smartgh.model.sensor.factoryFunctions

/** Object that represents a factory for the different functions that can be applied to calculate the new value of the
  * temperature [[TemperatureSensor]].
  */
object FactoryFunctionsTemperatureSensor:
  private val areaFactor = 0.90
  private val environmentFactor = 0.10

  val computeTemperature: (Double, Double) => Double =
    (currentAreaVal, temperatureToReach) => (currentAreaVal * areaFactor) + (temperatureToReach * environmentFactor)
