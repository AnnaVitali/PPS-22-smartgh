package it.unibo.pps.smartgh.model.sensor.factoryFunctions

import scala.util.Random

/** Object that represents a factory for the different functions that can be applied to calculate the new brightness
  * value of the [[LuminositySensor]].
  */
object FactoryFunctionsLuminosity:
  private val minPercentage = 0.1
  private val maxPercentage = 0.3
  private val randomValue = Random(10)

  val computeLuminosityWithAreaGatesOpen: (Double, Int) => Double =
    (currentValEnvironment, currentLampBrightness) => currentValEnvironment + currentLampBrightness

  val computeLuminosityWithAreaGatesCloseAndUnshielded: (Double, Int) => Double =
    (currentValEnvironment, currentLampBrightness) =>
      currentValEnvironment - (minPercentage + (maxPercentage - minPercentage) * randomValue
        .nextDouble()) * currentValEnvironment + currentLampBrightness

  val computeLuminosityWithAreaGatesCloseAndShielded: Int => Double = concurrentLampBrightness =>
    concurrentLampBrightness.toDouble
