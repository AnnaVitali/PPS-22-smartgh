package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.AreaControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.time.Timer
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.component.GreenHouseDivisionMVC.GreenHouseDivisionMVCImpl
import it.unibo.pps.smartgh.view.component.AreaViewModule

/** Object that can be used to create a new instance of [[AreaMVCImpl]]. */
object AreaMVC:
  /** Create a new [[AreaMVCImpl]].
    * @param plant
    *   of the Area
    * @param simulationMVC
    *   the simulation MVC instance
    * @param greenHouseDivisionMVC
    *   instance of the [[GreenHouseDivisionMVC]]
    * @return
    *   a new instance of [[AreaMVCImpl]].
    */
  def apply(
      plant: Plant,
      simulationMVC: SimulationMVCImpl,
      greenHouseDivisionMVC: GreenHouseDivisionMVCImpl
  ): AreaMVCImpl =
    AreaMVCImpl(plant, simulationMVC, greenHouseDivisionMVC)

  /** Implementation of the area MVC.
    *
    * @param plant
    *   of the Area
    * @param simulationMVC
    *   the simulation MVC instance
    * @param greenHouseDivisionMVC
    *   instance of the [[GreenHouseDivisionMVC]]
    */
  class AreaMVCImpl(
      plant: Plant,
      override val simulationMVC: SimulationMVCImpl,
      greenHouseDivisionMVC: GreenHouseDivisionMVCImpl
  ) extends AreaModelModule.Interface
      with AreaViewModule.Interface
      with AreaControllerModule.Interface
      with SimulationMVC.Interface:

    override val areaModel: AreaModelModule.AreaModel =
      AreaImpl(plant, simulationMVC.simulationController.subscribeTimerValue)
    override val areaView: AreaViewModule.AreaView = AreaViewImpl()
    override val areaController: AreaControllerModule.AreaController = AreaControllerImpl(greenHouseDivisionMVC)

    /** Paint the area. */
    def paintArea(): Unit = areaController.paintArea()
