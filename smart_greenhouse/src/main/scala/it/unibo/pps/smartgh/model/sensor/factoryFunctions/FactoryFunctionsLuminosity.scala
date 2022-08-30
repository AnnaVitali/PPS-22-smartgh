package it.unibo.pps.smartgh.model.sensor.factoryFunctions

import scala.util.Random

/** Object that represents a factory for the different functions that can be applied to calculate the new brightness
  * value of the [[LuminositySensor]].
  */
object FactoryFunctionsLuminosity:
  private val MinPercentage = 0.01
  private val MaxPercentage = 0.05

  /** Update the luminosity value when gates are open. */
  val updateLuminosityWithAreaGatesOpen: (Double, Double) => Double = _ + _

  /** Update the luminosity value when gates are open and shields are up. */
  val updateLuminosityWithAreaGatesCloseAndUnshielded: (Double, Double) => Double =
    (currentValEnvironment, currentLampBrightness) =>
      currentValEnvironment - (Random
        .nextDouble() * (MaxPercentage - MinPercentage) + MinPercentage) * currentValEnvironment + currentLampBrightness

  /** Update the luminosity value when area gates are closed and shields are down. */
  val updateLuminosityWithAreaGatesCloseAndShielded: Double => Double = currentLampBrightness => currentLampBrightness
