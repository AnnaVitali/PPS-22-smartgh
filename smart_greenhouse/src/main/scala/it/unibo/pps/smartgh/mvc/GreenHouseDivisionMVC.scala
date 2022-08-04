package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.GHControllerModule
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.view.component.GHViewModule

/** Object that can be used to create a new instance of [[GreenHouseDivisionMVC]]. */
object GreenHouseDivisionMVC:
  /** Create a new [[GreenHouseDivisionMVCImpl]].
   * @return
   *   a new instance of [[GreenHouseDivisionMVCImpl]].
   */
  def apply(plants: List[Plant]): GreenHouseDivisionMVCImpl = GreenHouseDivisionMVCImpl(plants)

  /** Create a new [[GreenHouseDivisionMVCImpl]].
   * @return
   *   a new instance of [[GreenHouseDivisionMVCImpl]].
   */
  class GreenHouseDivisionMVCImpl(plants: List[Plant])
    extends GHModelModule.Interface
      with GHViewModule.Interface
      with GHControllerModule.Interface:

    override val ghDivisionModel = GreenHouseImpl(plants)
    override val ghController = GreenHouseDivisionControllerImpl()
    override val ghDivisionView = GreenHouseDivisionViewImpl()

    def setAreas(): Unit =
      ghDivisionModel.areas = for p <- plants
        yield AreaMVC(p)


    def show(): Unit = ghController.updateView()









