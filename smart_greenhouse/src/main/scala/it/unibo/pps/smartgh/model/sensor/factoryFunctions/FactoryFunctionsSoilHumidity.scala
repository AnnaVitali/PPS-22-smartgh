package it.unibo.pps.smartgh.model.sensor.factoryFunctions

/** Object that represents a factory for the different functions that can be applied to calculate the new brightness
  * value of the [[SoilHumiditySensor]].
  */
object FactoryFunctionsSoilHumidity:

  private val valueRange = (0.0, 100.0)
  private val evaporationFactor = 100
  private val rainFactor = 0.50
  private val wateringFactor = 0.05
  private val movingSoilFactor = 0.10

  val updateValueWithEvaporation: Double => Double = currentAreaValue =>
    (currentAreaValue - currentAreaValue / evaporationFactor).max(valueRange._1)

  val updateValueWithAreaGatesOpen: (Double, Double) => Double = (currentAreaValue, precipitation) =>
    (currentAreaValue - currentAreaValue / evaporationFactor + precipitation * rainFactor)
      .max(valueRange._1)
      .min(valueRange._2)

  val updateValueWithWatering: Double => Double = currentAreaValue =>
    (currentAreaValue + currentAreaValue * wateringFactor).min(valueRange._2)

  val updateValueWithMovingSoil: Double => Double = currentAreaValue =>
    (currentAreaValue - currentAreaValue * movingSoilFactor).max(valueRange._1)