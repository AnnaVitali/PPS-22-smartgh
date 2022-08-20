package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.LoadingPlantControllerModule
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule.PlantSelectorModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.{BaseView, LoadingPlantViewModule}

/** Object that encloses the MVC structure for loading the plants data. */
object LoadingPlantMVC:

  /** Apply method for the [[LoadingPlantMVC]].
    * @param simulationMVC
    *   the root MVC of the application.
    * @param model
    *   the model for the [[LoadingPlantMVC]]
    * @param baseView
    *   the view in which the [[LoadingPlantView]] is enclosed.
    * @return
    *   the implementation of the loading plant MVC.
    */
  def apply(
      simulationMVC: SimulationMVCImpl,
      model: PlantSelectorModel,
      baseView: BaseView
  ): LoadingPlantMVCImpl =
    LoadingPlantMVCImpl(simulationMVC, model, baseView)

  /** Implementation of the [[LoadingPlantMVC]].
    * @param simulationMVC
    *   the root MVC of the application.
    * @param model
    *   the view in which the [[LoadingPlantView]] is enclosed.
    * @param baseView
    *   the view in which the [[LoadingPlantView]] is enclosed.
    */
  class LoadingPlantMVCImpl(
      simulationMVC: SimulationMVCImpl,
      model: PlantSelectorModel,
      baseView: BaseView
  ) extends PlantSelectorModelModule.Interface
      with LoadingPlantControllerModule.Interface
      with LoadingPlantViewModule.Interface:

    override val plantSelectorModel: PlantSelectorModelModule.PlantSelectorModel = model
    override val loadingPlantView: LoadingPlantViewModule.LoadingPlantView =
      LoadingPlantViewImpl(simulationMVC.simulationView, baseView)
    override val loadingPlantController: LoadingPlantControllerModule.LoadingPlantController =
      LoadingPlantControllerImpl(simulationMVC)

    loadingPlantController.setupBehaviour()
