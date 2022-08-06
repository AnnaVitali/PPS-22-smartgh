package it.unibo.pps.smartgh.model.sensor

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class LuminositySensorTest extends AnyFunSuite with Matchers:

  private val LS = "Luminosity sensor"
  private val initialLuminosity = 500
  private val luminositySensor = LuminositySensor(initialLuminosity)

  test(s"$LS must be initialized with a lower value then that of the environment") {
    luminositySensor.getCurrentValue should be < initialLuminosity
  }
