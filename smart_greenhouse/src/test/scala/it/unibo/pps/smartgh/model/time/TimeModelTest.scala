package it.unibo.pps.smartgh.model.time

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TimeModelTest extends AnyFunSuite with Matchers:

  private val timeModel: TimeModel = TimeModel()

  type TimeController = Any
  private var unsetTimeController: TimeController = _

  test("When timeModel is created, controller is not set") {
    timeModel.controller shouldEqual unsetTimeController
  }

  test("When controller has been set in timeModel, it should not be unset") {
    timeModel.controller = "prova"
    timeModel.controller should not equal unsetTimeController
  }
