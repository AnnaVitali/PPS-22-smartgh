package it.unibo.pps.smartgh.model.sensor

object FactoryFunctionsLuminosity:
  val computeLuminosityWithGreenhouseOpen: (currentValEnvironment: Int, currentLampBrightness: Int) => Int =
    ???
  val computeLuminosityWithGreenhouseCloseAndUnshild: (
      currentValEnvironment: Int,
      currentLampBrightness: Int
  ) => Int = ???
  val computeLuminosityWithGreenhouseCloseAndShild: (concurrentLampBrightness: Int) => Int = ???
