package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.SimulationControllerModule
import it.unibo.pps.smartgh.controller.component.EnvironmentControllerModule
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.{BaseView, EnvironmentViewModule}

/** Object that encloses the MVC structure for environment values management and time visualization. */
object EnvironmentMVC:

  /** Apply method for the [[EnvironmentMVC]]
    * @param simulationMVC
    *   the root MVC of the application.
    * @param baseView
    *   the view in which the [[EnvironmentView]] is enclosed.
    * @return
    *   the implementation of [[EnvironmentMVC]].
    */
  def apply(simulationMVC: SimulationMVCImpl): EnvironmentMVCImpl =
    EnvironmentMVCImpl(simulationMVC)

  /** Implementation of the environment MVC.
    * @param simulation
    *   the [[SimulationMVCImpl]] of the application.
    */
  class EnvironmentMVCImpl(simulation: SimulationMVCImpl)
      extends EnvironmentModelModule.Interface
      with EnvironmentViewModule.Interface
      with EnvironmentControllerModule.Interface
      with SimulationControllerModule.Interface
      with SimulationMVC.Interface:

    override val simulationMVC: SimulationMVCImpl = simulation
    override val simulationController: SimulationControllerModule.SimulationController = simulation.simulationController
    override val environmentModel: EnvironmentModelModule.EnvironmentModel = EnvironmentModelImpl(
      simulationController.environment
    ) //todo: enviroment?
    override val environmentView: EnvironmentViewModule.EnvironmentView = EnvironmentViewImpl()
    override val environmentController: EnvironmentControllerModule.EnvironmentController = EnvironmentControllerImpl()

    simulation.simulationController.environmentController = environmentController
    environmentController.startSimulation()
