package it.unibo.pps.smartgh.model.sensor.factoryFunctions

/** Object that represents a factory for the different functions that can be applied to calculate the new brightness
  * value of the [[AirHumiditySensor]].
  */
object FactoryFunctionsAirHumidity:

  private val valueRange = (0.0, 100.0)
  private val actionFactor = 1 / 100
  private val areaFactor = 90 / 100
  private val envFactor = 10 / 100

  val updateVentilationValue: (Double, Double) => Double = (currentValue, minValue) =>
    (currentValue - currentValue * actionFactor).max(minValue)

  val updateAtomizeValue: (Double, Double) => Double = (currentValue, maxValue) =>
    (currentValue - currentValue * actionFactor).min(maxValue)

  val updateDisableActionValue: (Double, Double, Double) => Double = (currentValue, envValue, randomVal) =>
    (currentValue * areaFactor + envValue * envFactor - randomVal).max(valueRange._1).min(valueRange._2)
