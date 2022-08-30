package it.unibo.pps.smartgh.model.sensor.factoryFunctions

import java.time.temporal.ValueRange
import scala.util.Random

/** Object that represents a factory for the different functions that can be applied to calculate the new air humidity
  * value of the [[AirHumiditySensor]].
  */
object FactoryFunctionsAirHumidity:

  private val ActionFactor = 0.05
  private val AreaFactor = 0.90
  private val EnvFactor = 0.10

  private def randActionFactor: Double = Random.nextDouble() * ActionFactor

  /** Update the ventilation value if it has not reached to the minimum value. */
  val updateVentilationValue: (Double, Double) => Double = (v, mn) => (v - randActionFactor).max(mn)

  /** Update the atomize value if it has not reached the maximum value */
  val updateAtomizeValue: (Double, Double) => Double = (v, mx) => (v + randActionFactor).min(mx)

  /** Update the action value when system actions are disabled. */
  val updateNoActionValue: (Double, Double, Double) => Double = _ * AreaFactor + _ * EnvFactor - _
