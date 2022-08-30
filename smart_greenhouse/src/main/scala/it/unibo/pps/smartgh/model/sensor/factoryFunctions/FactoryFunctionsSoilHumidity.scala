package it.unibo.pps.smartgh.model.sensor.factoryFunctions

/** Object that represents a factory for the different functions that can be applied to calculate the new soil humidity
  * value of the [[SoilHumiditySensor]].
  */
object FactoryFunctionsSoilHumidity:

  private val EvaporationFactor = 0.01
  private val RainFactor = 0.50
  private val WateringFactor = 0.05
  private val MovingSoilFactor = 0.10

  /** Update the soil moisture value without user's action. */
  val updateEvaporationValue: Double => Double = _ - EvaporationFactor

  /** Update the soil moisture value when area gates are open with a precipitation value. */
  val updateGatesOpenValue: (Double, Double) => Double = _ - EvaporationFactor + _ * RainFactor

  /** Update the soil moisture value with watering. */
  val updateWateringValue: Double => Double = _ + WateringFactor

  /** Update the soil moisture value when moving soil. */
  val updateMovingSoilValue: Double => Double = _ - MovingSoilFactor
