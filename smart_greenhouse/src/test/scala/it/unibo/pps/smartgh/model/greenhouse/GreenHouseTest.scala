package it.unibo.pps.smartgh.model.greenhouse

import it.unibo.pps.smartgh.controller.component.GHControllerModule
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.view.component.GHViewModule
import org.junit.jupiter.api.Assertions.assertTrue
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers

/** This class contains the tests realized to verify the correct behavior of [[GreenHouseTest]]. */
class GreenHouseTest extends AnyFunSuite with Matchers with GHModelModule.Interface:

  override val ghDivisionModel: GHModelModule.GreenHouseModel = GreenHouseDivisionModelImpl()

  test(s"greenhouse should have an empty areas List when created") {
    assertTrue(ghDivisionModel.areas.isEmpty)
  }
