package it.unibo.pps.smartgh.model.sensor.areaComponentsState

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class AreaComponentsStateTest extends AnyFunSuite with Matchers:

  private val areaComponentsState = AreaComponentsState()

  test("At the beginning the area should be closed with shild up and nebulizer and ventilazion down") {
    areaComponentsState.gatesState() shouldEqual AreaGatesState.Close
    areaComponentsState.shildState() shouldEqual AreaShildState.Up
    areaComponentsState.atomisingState() shouldEqual AreaAtomiseState.AtomisingInactive
    areaComponentsState.ventilationState() shouldEqual AreaVentilationState.VentilationInactive
  }

  test("At the beginning the lamp brightness should be initialized with the default value") {
    val defaultBrightnessValue = 100
    areaComponentsState.brightnessOfTheLamps() shouldEqual defaultBrightnessValue
  }
