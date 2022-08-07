package it.unibo.pps.smartgh.model.sensor.factoryFunctions

import scala.util.Random

object FactoryFunctionsLuminosity:
  private val minPercentage = 0.1
  private val maxPercentage = 0.3
  private val randomValue = Random(10)

  val computeLuminosityWithAreaGatesOpen: (currentValEnvironment: Double, currentLampBrightness: Int) => Double =
    (valEnv, lampBrightness) => valEnv + lampBrightness

  val computeLuminosityWithAreaGatesCloseAndUnshielded: (
      currentValEnvironment: Double,
      currentLampBrightness: Int
  ) => Double = (valEnv, lampBrightness) =>
    valEnv - (minPercentage + (maxPercentage - minPercentage) * randomValue.nextDouble()) * valEnv + lampBrightness

  val computeLuminosityWithAreaGatesCloseAndShielded: (concurrentLampBrightness: Int) => Double = lampBrightness =>
    lampBrightness.toDouble
