package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.LoadingPlantControllerModule
import it.unibo.pps.smartgh.controller.component.LoadingPlantControllerModule.LoadingPlantController
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule.PlantSelectorModel
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.component.LoadingPlantViewModule.LoadingPlantView
import it.unibo.pps.smartgh.view.component.LoadingPlantViewModule

/** A trait that represents the MVC component for the plant selector. */
trait LoadingPlantMVC
    extends PlantSelectorModelModule.Interface
    with LoadingPlantControllerModule.Interface
    with LoadingPlantViewModule.Interface
    with SimulationMVC.Interface

/** Object that encloses the MVC structure for loading the plants data. */
object LoadingPlantMVC:

  /** Apply method for the [[LoadingPlantMVC]].
    * @param simulationMVC
    *   the root MVC of the application.
    * @param model
    *   the model for the [[LoadingPlantMVC]]
    * @return
    *   the implementation of the loading plant MVC.
    */
  def apply(simulationMVC: SimulationMVC, model: PlantSelectorModel): LoadingPlantMVC =
    LoadingPlantMVCImpl(simulationMVC, model)

  private class LoadingPlantMVCImpl(override val simulationMVC: SimulationMVC, model: PlantSelectorModel)
      extends LoadingPlantMVC:

    override val plantSelectorModel: PlantSelectorModel = model
    override val loadingPlantView: LoadingPlantView = LoadingPlantViewImpl()
    override val loadingPlantController: LoadingPlantController = LoadingPlantControllerImpl()

    loadingPlantController.setupBehaviour()
