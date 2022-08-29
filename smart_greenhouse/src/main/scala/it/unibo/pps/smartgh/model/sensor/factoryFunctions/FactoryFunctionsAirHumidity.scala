package it.unibo.pps.smartgh.model.sensor.factoryFunctions

import scala.util.Random

/** Object that represents a factory for the different functions that can be applied to calculate the new air humidity
  * value of the [[AirHumiditySensor]].
  */
object FactoryFunctionsAirHumidity:

  private val ValueRange = (0.0, 100.0)
  private val ActionFactor = 0.05
  private val AreaFactor = 0.90
  private val EnvFactor = 0.10

  /** Update the ventilation value. */
  val updateVentilationValue: (Double, Double) => Double = (value, min) =>
    (value - value * ActionFactor).max(min).max(ValueRange._1)

  /** Update the atomize value. */
  val updateAtomizeValue: (Double, Double) => Double =
    (value, max) => (value + value * ActionFactor).min(max).min(ValueRange._2)

  /** Update the action value when system actions are disabled. */
  val updateNoActionValue: (Double, Double, Double) => Double = (value, envValue, randomVal) =>
    (value * AreaFactor + envValue * EnvFactor - randomVal).max(ValueRange._1).min(ValueRange._2)
