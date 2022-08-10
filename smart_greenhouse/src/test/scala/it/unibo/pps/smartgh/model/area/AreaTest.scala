package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.time.Timer
import org.scalatest.funsuite.AnyFunSuite
import scala.concurrent.duration.*

import scala.language.postfixOps

/** This class contains the tests to verify that the [[AreaModelModule]] works correctly. */
class AreaTest extends AnyFunSuite with AreaModelModule.Interface:

  private val timer = Timer(1 day)
  timer.start(println("time is up!"))
  override val areaModel: AreaModelModule.AreaModel = AreaImpl(Plant("lemon", "citrus limon"), timer)

  test("After create an area in which there is a lemon plant, The area plant's name must be lemon") {
    import org.scalatest.matchers.must.Matchers.mustEqual
    areaModel.plant.name mustEqual "lemon"
  }

  test("After create an area its state must be equal to Normal") {
    import org.scalatest.matchers.must.Matchers.mustEqual
    areaModel.status mustEqual AreaModelModule.AreaStatus.NORMAL
  }

  test("An area must have 4 sensors") {
    import org.scalatest.matchers.must.Matchers.mustEqual
    areaModel.sensors.size mustEqual 4
  }
