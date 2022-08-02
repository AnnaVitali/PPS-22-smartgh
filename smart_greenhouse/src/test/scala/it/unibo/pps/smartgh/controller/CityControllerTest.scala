package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.controller.CityController
import it.unibo.pps.smartgh.model.city.*
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

import scala.collection.immutable.HashMap
/** This class contains the tests to verify that the [[CityController]] works correctly. */
class CityControllerTest extends AnyFunSuite with Matchers with BeforeAndAfter:
  val city: City = City("Rome")
  val controller = CityController()

  test("selecting Rome as city must create a Rome city object") {
    controller.saveCity("Rome") == city
  }
