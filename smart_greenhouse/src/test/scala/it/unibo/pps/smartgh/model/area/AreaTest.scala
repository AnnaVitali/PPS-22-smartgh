package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.plants.Plant
import org.scalatest.funsuite.AnyFunSuite

/** This class contains the tests to verify that the [[AreaModelModule]] works correctly. */
class AreaTest extends AnyFunSuite with AreaModelModule.Interface:

  override val areaModel = AreaImpl(Plant("lemon", "citrus limon"))

  test("After create an area in which there is a lemon plant, The area plant's name must be lemon"){
    import org.scalatest.matchers.must.Matchers.mustEqual
    areaModel.plant.name mustEqual "lemon"
  }

  test("After create an area its state must be equal to Normal"){
    import org.scalatest.matchers.must.Matchers.mustEqual
    areaModel.status mustEqual AreaModelModule.AreaStatus.NORMAL
  }

  test("An area must have 4 sensors"){
    import org.scalatest.matchers.must.Matchers.mustEqual
    areaModel.sensors.size mustEqual 4
  }

