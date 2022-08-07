package it.unibo.pps.smartgh.model.time

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

/** This class contains tests to verify that the [[TimeModel]] works correctly. */
class TimeModelTest extends AnyFunSuite with Matchers:

  private val timeModel: TimeModel = TimeModel()

  test("When timeModel is created, controller is not set") {
    timeModel.controller should be (null)
  }