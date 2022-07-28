package it.unibo.pps.smartgh.greenhouse

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

class GreenHouseTest extends AnyFunSuite with Matchers:

  private val greenHouse =
    GreenHouse(List("p1", "p2", "p3", "p4", "p5", "p6"), List("p1", "p2", "p3", "p4", "p5", "p6"), "Rome")

  test(s"greenhouse should have 2 rows and 3 columns") {
    greenHouse.dimension mustEqual (2, 3)
  }
