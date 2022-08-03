package it.unibo.pps.smartgh.model.greenhouse

import it.unibo.pps.smartgh.controller.GHControllerModule
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.view.component.GHViewModule

/** This class contains the tests realized to verify the correct behavior of [[GreenHouseTest]]. */
class GreenHouseTest extends AnyFunSuite with Matchers
  with GHModelModule.Interface
  with GHViewModule.Interface
  with GHControllerModule.Interface:

  override val model: GHModelModule.Model = GreenHouseImpl(List("p1", "p2"), List("p1", "p2"), "Rome")
  override val view: GHViewModule.View = GreenHouseDivisionViewImpl()
  override val controller: GHControllerModule.Controller = GreenHouseDivisionControllerImpl()


  test(s"greenhouse should have 1 rows and 2 columns") {
    model.dimension mustEqual (1, 2)
  }
