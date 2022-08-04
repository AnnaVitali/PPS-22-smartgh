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

    override val ghDivisionModel: GHModelModule.Model = GreenHouseImpl(plants)
    override val ghController: GHControllerModule.Controller = GreenHouseDivisionControllerImpl()
    override val ghDivisionView: GHViewModule.View = GreenHouseDivisionViewImpl()

    def setAreas(): Unit =
      ghDivisionModel.areas = for p <- plants
        yield AreaMVC.AreaMVCImpl(p)


    def show(): Unit = ghController.updateView()









