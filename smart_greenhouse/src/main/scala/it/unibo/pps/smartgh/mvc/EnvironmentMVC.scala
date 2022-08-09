package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.EnvironmentControllerModule
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule
import it.unibo.pps.smartgh.model.city.Environment
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.BaseView

/** Object that encloses the MVC structure for environment values and time management. */
object EnvironmentMVC:

  /** Apply method for the [[EnvironmentMVC]]
    * @param simulationView
    *   the root view of the application.
    * @param baseView
    *   the view in which the [[EnvironmentView]] is enclosed.
    * @return
    *   the implemntation of [[EnvironmentMVC]].
    */
  def apply(simulationView: SimulationView, baseView: BaseView, city: Environment, selectedPlants : List[Plant]): EnvironmentMVCImpl =
    EnvironmentMVCImpl(simulationView, baseView, city, selectedPlants)

  class EnvironmentMVCImpl(simulationView: SimulationView, baseView: BaseView, city : Environment, selectedPlants: List[Plant])
      extends EnvironmentModelModule.Interface
      with EnvironmentViewModule.Interface
      with EnvironmentControllerModule.Interface:

    override val environmentModel: EnvironmentModelModule.EnvironmentModel = EnvironmentModelImpl(city)
    override val environmentView: EnvironmentViewModule.EnvironmentView = EnvironmentViewImpl(simulationView, baseView)
    override val environmentController: EnvironmentControllerModule.EnvironmentController = EnvironmentControllerImpl(selectedPlants)

    environmentController.startSimulation()
