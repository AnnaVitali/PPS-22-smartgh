package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.GHControllerModule
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.view.component.GHViewModule


object GreenHouseDivisionMVC:

  def apply(plants: List[Plant]): GreenHouseDivisionMVCImpl = GreenHouseDivisionMVCImpl(plants)

  class GreenHouseDivisionMVCImpl(plants: List[Plant])
    extends GHModelModule.Interface
      with GHViewModule.Interface
      with GHControllerModule.Interface:

    override val model: GHModelModule.Model = GreenHouseImpl(List("p1", "p2"), List("p1", "p2"), "Rome")
    override val controller: GHControllerModule.Controller = GreenHouseDivisionControllerImpl()
    override val view: GHViewModule.View = GreenHouseDivisionViewImpl()


    def show(): Unit = controller.updateView()









