package it.unibo.pps.smartgh.city

import it.unibo.pps.smartgh.city.*
import it.unibo.pps.smartgh.city.City
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

import scala.collection.immutable.HashMap

class CityControllerTest extends AnyFunSuite with Matchers with BeforeAndAfter:
  val city: City = City("Rome")
  val controller = CityController()

  test("selecting Rome as city must create a Rome city object") {
     controller.saveCity("Rome") == city
  }