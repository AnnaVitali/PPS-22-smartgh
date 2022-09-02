package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.Config
import it.unibo.pps.smartgh.controller.component.PlantSelectorControllerModule
import it.unibo.pps.smartgh.controller.component.PlantSelectorControllerModule.PlantSelectorController
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule.PlantSelectorModel
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule.SelectPlantView
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule

/** A trait that represents the MVC component for the plant selector. */
trait PlantSelectorMVC
    extends PlantSelectorModelModule.Interface
    with PlantSelectorControllerModule.Interface
    with SelectPlantViewModule.Interface
    with SimulationMVC.Interface

/** Object that encloses the MVC structure for the plant selection. */
object PlantSelectorMVC:

  /** Apply method for the [[PlantSelectorMVC]].
    * @param simulationMVC
    *   the root MVC of the application.
    * @return
    *   the implementation of the plant selection MVC.
    */
  def apply(simulationMVC: SimulationMVC): PlantSelectorMVC = PlantSelectorMVCImpl(simulationMVC)

  private class PlantSelectorMVCImpl(override val simulationMVC: SimulationMVC) extends PlantSelectorMVC:

    override val plantSelectorModel: PlantSelectorModel = PlantSelectorModelImpl(Config.Path + Config.PlantsOutputFile)
    override val selectPlantView: SelectPlantView = SelectPlantViewImpl()
    override val plantSelectorController: PlantSelectorController = PlantSelectorControllerImpl()

    plantSelectorController.setupBehaviour()
