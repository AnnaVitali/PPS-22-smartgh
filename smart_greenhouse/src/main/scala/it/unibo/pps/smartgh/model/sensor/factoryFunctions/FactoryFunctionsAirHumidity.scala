package it.unibo.pps.smartgh.model.sensor.factoryFunctions

import java.time.temporal.ValueRange
import scala.util.Random

/** Object that represents a factory for the different functions that can be applied to calculate the new air humidity
  * value of the [[AirHumiditySensor]].
  */
object FactoryFunctionsAirHumidity:

  private val AreaFactor = 0.9
  private val EnvFactor = 0.1

  private def randActionFactor: Double => Double = Random.nextDouble() * _ + EnvFactor

  /** Update the ventilation value if it has not reached to the maximum value. */
  val updateVentilationValue: (Double, Double, Double) => Double = (v, act, mx) => (v - randActionFactor(act)).max(mx)

  /** Update the atomize value if it has not reached the maximum value */
  val updateAtomizeValue: (Double, Double, Double) => Double = (v, act, mx) => (v + randActionFactor(act)).min(mx)

  /** Update the action value when system actions are disabled. */
  val updateNoActionValue: (Double, Double, Double) => Double = _ * AreaFactor + _ * EnvFactor - _
