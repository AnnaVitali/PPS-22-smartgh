package it.unibo.pps.smartgh.model.city

import it.unibo.pps.smartgh.model.greenhouse.Environment
import org.scalatest.BeforeAndAfter
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.matchers.should.Matchers.shouldNot
import org.scalatest.time.{Milliseconds, Span}
import org.scalatest.concurrent.Futures.timeout

import java.time.{LocalDateTime, ZonedDateTime}
import java.time.format.DateTimeFormatter
import scala.collection.immutable.HashMap

/** This class contains the tests to verify that the [[Environment]] works correctly. */
class EnvironmentTest extends AnyFunSuite with Matchers with BeforeAndAfter:
  val environment: Environment = Environment("Rome")

  test("after creating Rome city when we ask its name must it should be equal to Rome") {
    environment.nameCity shouldEqual "Rome"
  }

  test("after creating Rome city when we ask its environmental values they should not be empty ") {
    environment.currentEnvironmentValues should not equal Map.empty[String, Any]
  }

  test("after creating Rome city its environmental values should contain locality region equals to Lazio") {
    eventually(timeout(Span(1000, Milliseconds))) {
      val location: Map[String, Any] = environment.environmentValues("location").asInstanceOf[Map[String, Any]]
      location.get("region") shouldEqual Some("Lazio")
    }
  }

  test("after creating Rome city if I ask the main environmental value for hour 05:00 the output should contain time = current date 05:00") {
    val t2 = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now())
    environment.updateCurrentEnvironmentValues(5)
    environment.currentEnvironmentValues.getOrElse("time", "") shouldEqual t2 + " 05:00"
  }
