package it.unibo.pps.smartgh.model.greenhouse

import it.unibo.pps.smartgh.controller.GHControllerModule
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.view.component.GHViewModule

/** This class contains the tests realized to verify the correct behavior of [[GreenHouseTest]]. */
class GreenHouseTest extends AnyFunSuite with Matchers
  with GHModelModule.Interface:

  override val ghDivisionModel: GHModelModule.GreenHouseModel = GreenHouseImpl(List(Plant("lemon", "citrus limon"), Plant("mint", "mentha x gracilis")))

  test(s"greenhouse should have 1 rows and 2 columns") {
    ghDivisionModel.dimension mustEqual (1, 2)
  }
