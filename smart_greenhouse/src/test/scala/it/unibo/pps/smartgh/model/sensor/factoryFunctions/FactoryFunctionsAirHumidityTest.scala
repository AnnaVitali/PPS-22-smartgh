package it.unibo.pps.smartgh.model.sensor.factoryFunctions

import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.language.postfixOps
import scala.util.Random

/** This class contains the tests realized to verify that the [[FactoryFunctionsAirHumidity]] work correctly. */
class FactoryFunctionsAirHumidityTest extends AnyFunSuite with Matchers with BeforeAndAfter:

  private val factory = FactoryFunctionsAirHumidity
  private val nUpdates = 1 to 10
  private val valueRange = (0.0, 100.0)
  private val actionFactory = 2.0
  private var initialValue: Double = _
  private var currentValue: Double = _

  private def basicValueTest[A](value: A, function: A => Double): Double =
    val updatedValue = function(value)
    updatedValue should not be initialValue
    currentValue should (be > valueRange._1 and be < valueRange._2)
    updatedValue

  before {
    initialValue = 30.0
    currentValue = initialValue
  }

  test("updateVentilationValue should modify the value if the value is greater than minValue") {
    val minValue = initialValue - 2.0

    nUpdates.foreach { _ =>
      currentValue = basicValueTest((initialValue, actionFactory, minValue), factory.updateVentilationValue.tupled)
      if currentValue > minValue then initialValue = currentValue
    }
  }

  test("updateAtomizeValue should modify the value if the value is less than maxValue") {
    val maxValue = initialValue + 2.0

    nUpdates.foreach { _ =>
      currentValue = basicValueTest((initialValue, actionFactory, maxValue), factory.updateAtomizeValue.tupled)
      if currentValue < maxValue then initialValue = currentValue
    }
  }

  test("updateDisableActionValue should gradually become the value of the envValue") {
    val envValue = initialValue - 2.0
    val randomVal = 0.0

    (1 to 50).foreach { _ =>
      currentValue = basicValueTest((initialValue, envValue, randomVal), factory.updateNoActionValue.tupled)
      currentValue should (be < initialValue and be >= envValue - randomVal)
      initialValue = currentValue
    }
    currentValue should be(envValue +- 0.15)
  }
