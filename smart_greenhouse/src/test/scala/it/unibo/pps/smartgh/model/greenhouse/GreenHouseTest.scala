package it.unibo.pps.smartgh.model.greenhouse

import it.unibo.pps.smartgh.controller.component.GHControllerModule
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.view.component.GHViewModule
import org.junit.jupiter.api.Assertions.assertFalse

/** This class contains the tests realized to verify the correct behavior of [[GreenHouseTest]]. */
class GreenHouseTest extends AnyFunSuite with Matchers with GHModelModule.Interface:

  override val ghDivisionModel: GHModelModule.GreenHouseModel = GreenHouseImpl(
    List(Plant("lemon", "citrus limon"), Plant("mint", "mentha x gracilis"))
  )

  test(s"greenhouse should have a non empty plants list") {
    assertFalse(ghDivisionModel.plants.isEmpty)
  }
