package it.unibo.pps.smartgh.model.sensor.factoryFunctions

/** Object that represents a factory for the different functions that can be applied to calculate the new soil humidity
  * value of the [[SoilHumiditySensor]].
  */
object FactoryFunctionsSoilHumidity:

  private val EvaporationFactor = 0.01
  private val RainFactor = 0.50
  private val WateringFactor = 0.05
  private val MovingSoilFactor = 0.10

  /** Update the current soil moisture value with the evaporation factor. */
  val updateEvaporationValue: Double => Double = _ - EvaporationFactor

  /** Updates the current soil moisture value according to the precipitation value when gates are open. */
  val updateGatesOpenValue: (Double, Double) => Double = _ + _ * RainFactor

  /** Update the current soil moisture value with watering. */
  val updateWateringValue: Double => Double = _ + WateringFactor

  /** Update the current soil moisture value when moving soil. */
  val updateMovingSoilValue: Double => Double = _ - MovingSoilFactor
