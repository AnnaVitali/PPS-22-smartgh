package it.unibo.pps.smartgh.model.greenhouse

import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule.EnvironmentModel
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.concurrent.Futures.timeout
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.scalatest.time.{Milliseconds, Span}

import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

class EnvironmentModelTest extends AnyFunSuite with Matchers with EnvironmentModelModule.Interface:

  val environment : Environment = Environment("Rome", "41.8931", "12.4828")
  val environmentModel : EnvironmentModel = EnvironmentModelImpl(environment)

  test("If I ask for environment values at 05:00 o' clock, the output should contain time = current date 05:00") {
    eventually(timeout(Span(3000, Milliseconds))) {
      val t2 = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now())
      environmentModel.updateCurrentEnvironmentValues(5)
      environmentModel.currentEnvironmentValues.getOrElse("time", "") shouldEqual t2 + " 05:00"
    }
  }

  test("If I ask for environment values at 02:00 o' clock, the output should contain lux = 0") {
    eventually(timeout(Span(3000, Milliseconds))) {
      environmentModel.updateCurrentEnvironmentValues(2)
      environmentModel.currentEnvironmentValues.getOrElse("lux", "") shouldEqual 0
    }
  }