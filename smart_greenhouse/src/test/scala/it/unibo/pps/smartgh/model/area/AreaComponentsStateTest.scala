package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.area.AreaComponentsState.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** This class contains the tests realized to verify that [[AreaComponentsState]] behaves correctly. */
class AreaComponentsStateTest extends AnyFunSuite with Matchers:

  private val areaComponentsState = AreaComponentsState()

  test("At the beginning the area should be closed with shield up and atomiser and ventilation down") {
    areaComponentsState.gatesState shouldEqual AreaGatesState.Open
    areaComponentsState.shieldState shouldEqual AreaShieldState.Up
    areaComponentsState.atomisingState shouldEqual AreaAtomiseState.AtomisingInactive
    areaComponentsState.ventilationState shouldEqual AreaVentilationState.VentilationInactive
  }

  test("At the beginning the lamp brightness should be initialized with the default value") {
    val defaultBrightnessValue = 3000.0
    areaComponentsState.brightnessOfTheLamps shouldEqual defaultBrightnessValue
  }

  test("At the beginning the temperature should be initialized with the default value") {
    val defaultTemperatureValue = 27
    areaComponentsState.temperature shouldEqual defaultTemperatureValue
  }

  test("At the beginning the humidity should be between the maximum and minimum values") {
    val maxInitialHumidity = 30.0
    val minInitialHumidity = 10.0
    areaComponentsState.soilHumidity should (be >= minInitialHumidity and be <= maxInitialHumidity)
  }

  test("When a new action by the user is performed the area components state should change") {
    val newInternalTemperature = 30.0
    val newLampBrightness = 500
    areaComponentsState.shieldState = AreaShieldState.Up
    areaComponentsState.shieldState shouldEqual AreaShieldState.Up

    areaComponentsState.temperature = newInternalTemperature
    areaComponentsState.temperature shouldEqual newInternalTemperature

    areaComponentsState.brightnessOfTheLamps = newLampBrightness
    areaComponentsState.brightnessOfTheLamps shouldEqual newLampBrightness
  }
