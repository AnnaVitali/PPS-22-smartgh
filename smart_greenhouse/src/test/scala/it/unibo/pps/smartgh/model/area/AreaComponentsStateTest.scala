package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.area.AreaComponentsState.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** This class contains the tests realized to verify that [[AreaComponentsState]] behaves correctly. */
class AreaComponentsStateTest extends AnyFunSuite with Matchers:

  private val areaComponentsState = AreaComponentsState()

  test("At the beginning the area should be closed with shield up and atomiser and ventilation down") {
    areaComponentsState.gatesState shouldEqual AreaGatesState.Close
    areaComponentsState.shieldState shouldEqual AreaShieldState.Up
    areaComponentsState.atomisingState shouldEqual AreaAtomiseState.AtomisingInactive
    areaComponentsState.ventilationState shouldEqual AreaVentilationState.VentilationInactive
  }

  test("At the beginning the lamp brightness should be initialized with the default value") {
    val defaultBrightnessValue = 100
    areaComponentsState.brightnessOfTheLamps shouldEqual defaultBrightnessValue
  }

  test("At the beginning the temperature should be initialized with the default value") {
    val defaultTemperatureValue = 27
    areaComponentsState.temperature shouldEqual defaultTemperatureValue
  }

  test("When a new action bby the user is performed the area components state should change") {
    val newInternalTemperature = 30.0
    val newLampBrightness = 500
    areaComponentsState.shieldState = AreaShieldState.Up
    areaComponentsState.shieldState shouldEqual AreaShieldState.Up

    areaComponentsState.temperature = newInternalTemperature
    areaComponentsState.temperature shouldEqual newInternalTemperature

    areaComponentsState.brightnessOfTheLamps = newLampBrightness
    areaComponentsState.brightnessOfTheLamps shouldEqual newLampBrightness
  }
