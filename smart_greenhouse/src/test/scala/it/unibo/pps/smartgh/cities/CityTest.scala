package it.unibo.pps.smartgh.cities

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.shouldNot
import it.unibo.pps.smartgh.cities.*
import org.scalatest.BeforeAndAfter

import scala.collection.immutable.HashMap

class CityTest extends AnyFunSuite with Matchers with BeforeAndAfter:
  val city: City = CityImpl("Rome")

  test("after creating Rome city when we ask its name must it should be equal to Rome"){
    city.name shouldEqual "Rome"
  }

  test("after creating Rome city when we ask its environmental values they should not be empty "){
    city.environmentValues should not equal Map.empty[String, Any]
  }

  test("after creating Rome city its environmental values should contain locality region equals to Lazio"){
    val location: Map[String, Any] = city.environmentValues("location").asInstanceOf[Map[String, Any]]
    location.get("region") shouldEqual Some("Lazio")
  }

