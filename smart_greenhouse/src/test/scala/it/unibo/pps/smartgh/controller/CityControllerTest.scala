package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.controller.CitySearcherControllerModule.{CitySearcherController, Component}
import it.unibo.pps.smartgh.model.city.*
import it.unibo.pps.smartgh.model.city.CitySearcherModelModule.CitySearcherModel
import it.unibo.pps.smartgh.mvc.MVCCitySearcher
import it.unibo.pps.smartgh.mvc.MVCCitySearcher.MVCCitySearcherImpl
import it.unibo.pps.smartgh.view.component.{AbstractViewTest, BaseView, CitySearcherViewModule}
import it.unibo.pps.smartgh.view.component.CitySearcherViewModule.CitySearcherView
import javafx.stage.Stage
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.scalatest.BeforeAndAfter
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers
import org.testfx.framework.junit5.{ApplicationExtension, Start}

import scala.collection.immutable.HashMap

/** This class contains the tests to verify that the [[CityController]] works correctly. */
class CityControllerTest
    extends AnyFunSuite
    with Matchers
    with BeforeAndAfter
    with CitySearcherControllerModule.Interface
    with CitySearcherViewModule.Interface
    with CitySearcherModelModule.Interface:

  override val citySearcherController: CitySearcherController = CitySearcherControllerImpl()
  override val citySearcherModel: CitySearcherModel = null
  override val citySearcherView: CitySearcherView = null

  val city: City = City("Rome")

  test("selecting Rome as city must create a Rome city object") {
    citySearcherController.saveCity("Rome") === city
  }
