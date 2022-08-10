package it.unibo.pps.smartgh.model.sensor.factoryFunctions

/** Object that represents a factory for the different functions that can be applied to calculate the new air humidity
  * value of the [[AirHumiditySensor]].
  */
object FactoryFunctionsAirHumidity:

  private val valueRange = (0.0, 100.0)
  private val actionFactor = 0.05
  private val areaFactor = 0.90
  private val envFactor = 0.10

  /** Update the ventilation value. */
  val updateVentilationValue: (Double, Double) => Double = (currentValue, minValue) =>
    (currentValue - currentValue * actionFactor).max(minValue)

  /** Update the atomize value. */
  val updateAtomizeValue: (Double, Double) => Double = (currentValue, maxValue) =>
    (currentValue + currentValue * actionFactor).min(maxValue)

  /** Update the action value when system actions are disabled. */
  val updateDisableActionValue: (Double, Double, Double) => Double = (currentValue, envValue, randomVal) =>
    (currentValue * areaFactor + envValue * envFactor - randomVal).max(valueRange._1).min(valueRange._2)
