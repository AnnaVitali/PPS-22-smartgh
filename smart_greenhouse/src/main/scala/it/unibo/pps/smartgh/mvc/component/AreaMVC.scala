package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.AreaControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.time.Timer
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.AreaViewModule

/** Object that can be used to create a new instance of [[AreaMVCImpl]]. */
object AreaMVC:
  /** Create a new [[AreaMVCImpl]].
    * @param plant
    *   of the Area
    * @param timer
    *   instance of the simulation [[Timer]]
    * @param simulationMVC
    *   the simulation MVC instance
    * @return
    *   a new instance of [[AreaMVCImpl]].
    */
  def apply(plant: Plant, timer: Timer, simulationMVC: SimulationMVCImpl): AreaMVCImpl =
    AreaMVCImpl(plant, timer, simulationMVC)

  /** Implementation of the area MVC.
    * @param plant
    *   of the Area
    * @param simulationMVC
    *   the simulation MVC instance
    * @param timer
    *   instance of the simulation [[Timer]]
    */
  class AreaMVCImpl(plant: Plant, timer: Timer, simulationMVC: SimulationMVCImpl)
      extends AreaModelModule.Interface
      with AreaViewModule.Interface
      with AreaControllerModule.Interface:
    override val areaModel: AreaModelModule.AreaModel = AreaImpl(plant, timer)
    override val areaView: AreaViewModule.AreaView = AreaViewImpl(simulationMVC.simulationView)
    override val areaController: AreaControllerModule.AreaController = AreaControllerImpl(simulationMVC)

    /** Paint the area. */
    def paintArea(): Unit = areaController.paintArea()
