package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.SimulationControllerModule
import it.unibo.pps.smartgh.controller.component.EnvironmentControllerModule
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule
/** A trait that represents the MVC component for the environment. */
trait EnvironmentMVC
    extends EnvironmentModelModule.Interface
    with EnvironmentViewModule.Interface
    with EnvironmentControllerModule.Interface
    with SimulationMVC.Interface

/** Object that encloses the MVC structure for environment values management and time visualization. */
object EnvironmentMVC:

  /** Apply method for the [[EnvironmentMVC]]
    * @param simulationMVC
    *   the root MVC of the application.
    * @return
    *   the implementation of [[EnvironmentMVC]].
    */
  def apply(simulationMVC: SimulationMVC): EnvironmentMVC =
    EnvironmentMVCImpl(simulationMVC)

  private class EnvironmentMVCImpl(override val simulationMVC: SimulationMVC) extends EnvironmentMVC:

    override val environmentModel: EnvironmentModelModule.EnvironmentModel = EnvironmentModelImpl(
      simulationMVC.simulationController.environment
    )
    override val environmentView: EnvironmentViewModule.EnvironmentView = EnvironmentViewImpl()
    override val environmentController: EnvironmentControllerModule.EnvironmentController = EnvironmentControllerImpl()

    simulationMVC.simulationController.environmentController = environmentController
    environmentController.startSimulation()
