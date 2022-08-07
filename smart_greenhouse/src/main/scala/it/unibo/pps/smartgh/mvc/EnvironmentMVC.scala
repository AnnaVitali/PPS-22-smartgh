package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.EnvironmentControllerModule
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule
import it.unibo.pps.smartgh.model.city.City
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule

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
  def apply(simulationView: SimulationView, baseView: BaseView): EnvironmentMVCImpl =
    EnvironmentMVCImpl(simulationView, baseView)

  class EnvironmentMVCImpl(simulationView: SimulationView, baseView: BaseView)
      extends EnvironmentModelModule.Interface
      with EnvironmentViewModule.Interface
      with EnvironmentControllerModule.Interface:

    override val environmentModel: EnvironmentModelModule.EnvironmentModel = EnvironmentModelImpl(City("Rome"))
    override val environmentView: EnvironmentViewModule.EnvironmentView = EnvironmentViewImpl(simulationView, baseView)
    override val environmentController: EnvironmentControllerModule.EnvironmentController = EnvironmentControllerImpl()

    environmentController.startSimulation()
