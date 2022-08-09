package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.EnvironmentControllerModule
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule
import it.unibo.pps.smartgh.model.city.Environment
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.controller.SimulationControllerModule
import it.unibo.pps.smartgh.controller.SimulationControllerModule.SimulationController
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.BaseView
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl

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
  def apply(
      simulationMVC: SimulationMVCImpl,
      baseView: BaseView
  ): EnvironmentMVCImpl =
    EnvironmentMVCImpl(simulationMVC, baseView)

  class EnvironmentMVCImpl(
      simulationMVC: SimulationMVCImpl,
      baseView: BaseView
  ) extends EnvironmentModelModule.Interface
      with EnvironmentViewModule.Interface
      with EnvironmentControllerModule.Interface
      with SimulationControllerModule.Interface:

    override val simulationController: SimulationControllerModule.SimulationController = simulationMVC.simulationController
    override val environmentModel: EnvironmentModelModule.EnvironmentModel = EnvironmentModelImpl(simulationController.environment)
    override val environmentView: EnvironmentViewModule.EnvironmentView = EnvironmentViewImpl(simulationMVC.simulationView, baseView)
    override val environmentController: EnvironmentControllerModule.EnvironmentController = EnvironmentControllerImpl(simulationMVC)
    
    environmentController.startSimulation()
