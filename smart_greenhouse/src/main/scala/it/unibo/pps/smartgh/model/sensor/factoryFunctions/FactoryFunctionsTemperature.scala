package it.unibo.pps.smartgh.model.sensor.factoryFunctions

/** Object that represents a factory for the different functions that can be applied to calculate the new value of the
  * temperature [[TemperatureSensor]].
  */
object FactoryFunctionsTemperature:

  private val AreaFactor = 0.90
  private val EnvironmentFactor = 0.10

  /** Update temperature value. */
  val updateTemperatureApproachingTemperatureToReach: (Double, Double) => Double =
    _ * AreaFactor + _ * EnvironmentFactor
