package it.unibo.pps.smartgh.model.sensor.factoryFunctions

/** Object that represents a factory for the different functions that can be applied to calculate the new value of the
  * temperature [[TemperatureSensor]].
  */
object FactoryFunctionsTemperature:
  private val areaFactor = 0.90
  private val environmentFactor = 0.10

  val updateTemperature: (Double, Double) => Double =
    (currentAreaVal, temperatureToReach) => (currentAreaVal * areaFactor) + (temperatureToReach * environmentFactor)
