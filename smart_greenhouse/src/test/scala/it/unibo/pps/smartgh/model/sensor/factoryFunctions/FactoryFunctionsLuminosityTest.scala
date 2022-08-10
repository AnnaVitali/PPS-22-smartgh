package it.unibo.pps.smartgh.model.sensor.factoryFunctions

import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps
import scala.util.Random

/** This class contains the tests realized to verify that the [[FactoryFunctionsLuminosity]] work correctly. */
class FactoryFunctionsLuminosityTest extends AnyFunSuite with Matchers with BeforeAndAfter:

  private val factory = FactoryFunctionsLuminosity
  private val minBrightness = 100.0
  private var currentBrightness: Double = _

  test("computeLuminosityWithAreaGatesCloseAndShielded should modify the brightness to the passed value") {
    val lampBrightness = 2000.0
    currentBrightness = factory.updateLuminosityWithAreaGatesCloseAndShielded(lampBrightness)
    currentBrightness should (be >= minBrightness and equal(lampBrightness))
  }

  test(
    "computeLuminosityWithAreaGatesCloseAndUnshielded should modify the brightness in order to be less or equal to the " +
      "sum between the lamp brightness the environment brightness"
  ) {
    val lampBrightness = 2000.0
    val environmentBrightness = 4000.0
    currentBrightness = factory.updateLuminosityWithAreaGatesCloseAndUnshielded(environmentBrightness, lampBrightness)
    currentBrightness should be <= (environmentBrightness + lampBrightness)

  }

  test(
    "computeLuminosityWithAreaGatesOpen should modify the brightness in order to be the sum between the lamp " +
      "brightness and the environment brightness"
  ) {
    val lampBrightness = 2000.0
    val environmentBrightness = 2000.0
    currentBrightness = factory.updateLuminosityWithAreaGatesOpen(environmentBrightness, lampBrightness)
    currentBrightness shouldEqual (environmentBrightness + lampBrightness)
  }
