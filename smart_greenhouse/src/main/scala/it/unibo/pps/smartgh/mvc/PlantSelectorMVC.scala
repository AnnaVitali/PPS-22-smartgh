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

/** Object that encloses the MVC structure for the plant selection. */
object PlantSelectorMVC:

  val filename: String = System.getProperty("user.home") + "/pps/plants.pl"

  /** Apply method for the [[PlantSelectorMVC]]
    * @param simulationView
    *   the root view of the application.
    * @param baseView
    *   the view in which the [[SelectPlantView]] is enclosed.
    * @return
    *   the implemntation of the plant selection MVC.
    */
  def apply(simulationView: SimulationView, baseView: BaseView): PlantSelectorMVCImpl =
    PlantSelectorMVCImpl(simulationView, baseView)

  class PlantSelectorMVCImpl(simulationView: SimulationView, baseView: BaseView)
      extends PlantSelectorModelModule.Interface
      with PlantSelectorControllerModule.Interface
      with SelectPlantViewModule.Interface:

    override val plantSelectorModel: PlantSelectorModel = PlantSelectorModelImpl(filename)
    override val selectPlantView: SelectPlantView = SelectPlantViewImpl(simulationView, baseView)
    override val plantSelectorController: PlantSelectorController = PlantSelectorControllerImpl()

    plantSelectorController.configureAvailablePlants()
