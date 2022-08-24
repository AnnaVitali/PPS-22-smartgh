package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.Config
import it.unibo.pps.smartgh.controller.component.PlantSelectorControllerModule
import it.unibo.pps.smartgh.controller.component.PlantSelectorControllerModule.PlantSelectorController
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule.PlantSelectorModel
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule.SelectPlantView
import it.unibo.pps.smartgh.view.component.{BaseView, SelectPlantViewModule}

/** Object that encloses the MVC structure for the plant selection. */
object PlantSelectorMVC:

  /** Apply method for the [[PlantSelectorMVC]].
    * @param simulationMVC
    *   the root MVC of the application.
    * @return
    *   the implementation of the plant selection MVC.
    */
  def apply(simulationMVC: SimulationMVCImpl): PlantSelectorMVCImpl = PlantSelectorMVCImpl(simulationMVC)

  /** Implementation of the [[PlantSelectorMVCImpl]].
    * @param simulation
    *   [[SimulationMVCImpl]] of the simulation
    */
  class PlantSelectorMVCImpl(simulation: SimulationMVCImpl)
      extends PlantSelectorModelModule.Interface
      with PlantSelectorControllerModule.Interface
      with SelectPlantViewModule.Interface
      with SimulationMVC.Interface:

    override val simulationMVC: SimulationMVCImpl = simulation
    override val plantSelectorModel: PlantSelectorModel = PlantSelectorModelImpl(Config.path + Config.plantsOutputFile)
    override val selectPlantView: SelectPlantView = SelectPlantViewImpl()
    override val plantSelectorController: PlantSelectorController = PlantSelectorControllerImpl()

    plantSelectorController.setupBehaviour()
