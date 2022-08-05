package it.unibo.pps.smartgh.model.sensor

object FactoryFunctionsSoilHumidity:

  private val valueRange = (0.0, 100.0)
  private val evaporationFactor = 100
  private val rainFactor = 40.0 / 100
  private val wateringFactor = 5.0 / 100
  private val movingSoilFactor = 10.0 / 100

  val updateValueWithEvaporation: Double => Double = (currentAreaValue: Double) =>
    (currentAreaValue - currentAreaValue / evaporationFactor).max(valueRange._1)

  val updateValueWithRain: (Double, Double) => Double = (currentAreaValue: Double, precipitation: Double) =>
    (currentAreaValue + precipitation * rainFactor).min(valueRange._2)

  val updateValueWithWatering: Double => Double = (currentAreaValue: Double) =>
    (currentAreaValue + currentAreaValue * wateringFactor).min(valueRange._2)

  val updateValueWithMovingSoil: Double => Double = (currentAreaValue: Double) =>
    (currentAreaValue - currentAreaValue * movingSoilFactor).max(valueRange._1)

  @main def evaporationTest(): Unit =
    println("----serra aperta/chiusa----")
    var value = 30.0
    (1 to 24).foreach(i => // OGNI ORA
      value = updateValueWithEvaporation(value)
      println(f"Ore $i: $value%.2f")
    )

  @main def rainingTest(): Unit =
    println("----serra aperta con pioggia ----")
    var value = 30.0
    (1 to 24).foreach(i => // OGNI ORA
      value = updateValueWithRain(value, 5.0)
      println(f"Ore $i: $value%.2f")
    )
