package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.GHControllerModule
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.view.component.GHViewModule


object GreenHouseDivisionMVC
  extends GHModelModule.Interface
    with GHViewModule.Interface
    with GHControllerModule.Interface:

  override val model: GHModelModule.Model = GreenHouseImpl(List("p1", "p2"), List("p1", "p2"), "Rome")
  override val view: GHViewModule.View = GreenHouseDivisionViewImpl()
  override val controller: GHControllerModule.Controller = GreenHouseDivisionControllerImpl()
  controller.updateView()









