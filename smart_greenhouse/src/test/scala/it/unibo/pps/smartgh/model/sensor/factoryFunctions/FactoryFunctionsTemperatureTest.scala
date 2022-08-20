package it.unibo.pps.smartgh.model.sensor.factoryFunctions

import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** This class contains the tests realized to verify that the [[FactoryFunctionTemperature]] work correctly. */
class FactoryFunctionsTemperatureTest extends AnyFunSuite with Matchers with BeforeAndAfter:

  private val factory = FactoryFunctionsTemperature
  private val nUpdates = 1 to 10
  private var initialValue: Double = 27.0
  private var currentTemperatureValue: Double = _

  private def basicValueTest[A](value: A, function: A => Double): Double =
    val updatedValue = function(value)
    updatedValue should not be initialValue
    updatedValue

  test(
    "computeTemperature given the current temperature value should modify it in order to approach " +
      "the temperature value to reach"
  ) {
    val temperatureToReach = 37.0
    nUpdates.foreach { _ =>
      currentTemperatureValue = basicValueTest((initialValue, temperatureToReach), factory.updateTemperatureApproachingTemperatureToReach.tupled)
      if currentTemperatureValue >= initialValue then initialValue = currentTemperatureValue
    }

  }
