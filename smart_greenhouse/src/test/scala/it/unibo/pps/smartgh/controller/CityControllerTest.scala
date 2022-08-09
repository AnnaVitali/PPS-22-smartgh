package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.controller.SelectCityControllerModule.{Component, SelectCityController}
import it.unibo.pps.smartgh.model.city.*
import it.unibo.pps.smartgh.model.city.SelectCityModelModule.SelectCityModel
import it.unibo.pps.smartgh.mvc.{SelectCityMVC, SimulationMVC}
import it.unibo.pps.smartgh.mvc.SelectCityMVC.SelectCityMVCImpl
import it.unibo.pps.smartgh.view.component.{BaseView, SelectCityViewModule}
import it.unibo.pps.smartgh.view.component.SelectCityViewModule.SelectCityView
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
    with SelectCityControllerModule.Interface
    with SelectCityViewModule.Interface
    with SelectCityModelModule.Interface:

  val simulationMVC = SimulationMVC(null)
  override val selectCityController: SelectCityController = SelectCityControllerImpl(simulationMVC)
  override val selectCityModel: SelectCityModel = null
  override val selectCityView: SelectCityView = null

  //val city: Environment = Environment("Rome")

  //TODO rivedere
  test("selecting Rome as city must create a Rome city object") {
    //selectCityController.saveCity("Rome")
    //simulationMVC.simulationController.environment === city
    println("TEST NOTHING TODO")
  }
