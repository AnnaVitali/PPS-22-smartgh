package it.unibo.pps.smartgh.model.sensor.factoryFunctions

/** Object that represents a factory for the different functions that can be applied to calculate the new soil humidity
  * value of the [[SoilHumiditySensor]].
  */
object FactoryFunctionsSoilHumidity:

  private val ValueRange = (0.0, 100.0)
  private val EvaporationFactor = 0.005
  private val RainFactor = 0.50
  private val WateringFactor = 0.05
  private val MovingSoilFactor = 0.10

  /** Update the soil moisture value without user's action. */
  val updateEvaporationValue: Double => Double = value => (value - value * EvaporationFactor).max(ValueRange._1)

  /** Update the soil moisture value when area gates are open. */
  val updateGatesOpenValue: (Double, Double) => Double = (value, precipitation) =>
    (value - value * EvaporationFactor + precipitation * RainFactor).max(ValueRange._1).min(ValueRange._2)

  /** Update the soil moisture value with watering. */
  val updateWateringValue: Double => Double = value => (value + value * WateringFactor).min(ValueRange._2)

  /** Update the soil moisture value when moving soil. */
  val updateMovingSoilValue: Double => Double = value => (value - value * MovingSoilFactor).max(ValueRange._1)
