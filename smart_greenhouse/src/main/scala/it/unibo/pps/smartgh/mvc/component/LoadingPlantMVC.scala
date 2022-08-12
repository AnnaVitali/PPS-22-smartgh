package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.LoadingPlantControllerModule
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule.PlantSelectorModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.{BaseView, LoadingPlantViewModule}

object LoadingPlantMVC:

  def apply(
      simulationMVC: SimulationMVCImpl,
      model: PlantSelectorModel,
      baseView: BaseView
  ): LoadingPlantMVCImpl =
    LoadingPlantMVCImpl(simulationMVC, model, baseView)

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

    loadingPlantController.registerModelCallback()
