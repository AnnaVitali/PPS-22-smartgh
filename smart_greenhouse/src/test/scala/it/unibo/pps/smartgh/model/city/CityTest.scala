package it.unibo.pps.smartgh.model.city

import it.unibo.pps.smartgh.model.city.*
import it.unibo.pps.smartgh.model.city.City
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.shouldNot

import scala.collection.immutable.HashMap
/** This class contains the tests to verify that the [[City]] works correctly. */
class CityTest extends AnyFunSuite with Matchers with BeforeAndAfter:
  val city: City = City("Rome")

  test("after creating Rome city when we ask its name must it should be equal to Rome") {
    city.name shouldEqual "Rome"
  }

  test("after creating Rome city when we ask its environmental values they should not be empty ") {
    city.environmentValues should not equal Map.empty[String, Any]
  }

  test("after creating Rome city its environmental values should contain locality region equals to Lazio") {
    val location: Map[String, Any] = city.environmentValues("location").asInstanceOf[Map[String, Any]]
    location.get("region") shouldEqual Some("Lazio")
  }
