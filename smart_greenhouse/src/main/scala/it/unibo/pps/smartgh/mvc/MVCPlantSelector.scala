package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule
import it.unibo.pps.smartgh.controller.PlantSelectorControllerModule
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule
import it.unibo.pps.smartgh.controller.PlantSelectorControllerModule.Interface
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule.Interface
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule.Interface
import it.unibo.pps.smartgh.model.plants.PlantSelectorModelModule.PlantSelectorModel
import it.unibo.pps.smartgh.controller.PlantSelectorControllerModule.PlantSelectorController
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule.SelectPlantView
import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.BaseView

object MVCPlantSelector:

  val filename: String = System.getProperty("user.home") + "pps/plants.pl"

  def apply(simulationView: SimulationView, baseView: BaseView): MVCPlantSelectorImpl =
    MVCPlantSelectorImpl(simulationView, baseView)

  class MVCPlantSelectorImpl(simulationView: SimulationView, baseView: BaseView)
      extends PlantSelectorModelModule.Interface
      with PlantSelectorControllerModule.Interface
      with SelectPlantViewModule.Interface:

    override val plantSelectorModel: PlantSelectorModel = PlantSelectorModelImpl(filename)
    override val plantSelectorController: PlantSelectorController = PlantSelectorControllerImpl()
    override val selectPlantView: SelectPlantView = SelectPlantViewImpl(simulationView, baseView)

    plantSelectorController.configureAvailablePlants()
