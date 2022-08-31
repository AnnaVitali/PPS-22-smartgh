package it.unibo.pps.smartgh.model.sensor.factoryFunctions

import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** This class contains the tests realized to verify that the [[FactoryFunctionsSoilHumidity]] work correctly. */
class FactoryFunctionsSoilHumidityTest extends AnyFunSuite with Matchers with BeforeAndAfter:

  private val factory = FactoryFunctionsSoilHumidity
  private val nUpdates = 1 to 10
  private val valueRange = (0.0, 100.0)
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

  test("updateValueWithEvaporation should modify the value correctly") {
    nUpdates.foreach { _ =>
      currentValue = basicValueTest(initialValue, factory.updateEvaporationValue)
      currentValue should be < initialValue
      initialValue = currentValue
    }
  }

  test("updateValueWithAreaGatesOpen should modify the value correctly") {
    val precipitation = 1.0
    nUpdates.foreach { _ =>
      currentValue = basicValueTest((initialValue, precipitation), factory.updateGatesOpenValue.tupled)
      currentValue should be > initialValue
      initialValue = currentValue
    }
  }

  test("updateValueWithWatering should modify the value correctly") {
    nUpdates.foreach { _ =>
      currentValue = basicValueTest(initialValue, factory.updateWateringValue)
      currentValue should be > initialValue
      initialValue = currentValue
    }
  }

  test("updateValueWithMovingSoil should modify the value") {
    nUpdates.foreach { _ =>
      currentValue = basicValueTest(initialValue, factory.updateMovingSoilValue)
      currentValue should be < initialValue
      initialValue = currentValue
    }
  }
