package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.EnvironmentControllerModule
import it.unibo.pps.smartgh.model.EnvironmentModelModule
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule
import it.unibo.pps.smartgh.model.city.City

import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.BaseView

object EnvironmentMVC:

  def apply(simulationView: SimulationView, baseView: BaseView): EnvironmentMVCImpl =
    EnvironmentMVCImpl(simulationView, baseView)

  class EnvironmentMVCImpl(simulationView: SimulationView, baseView: BaseView)
    extends EnvironmentModelModule.Interface
      with EnvironmentViewModule.Interface
      with EnvironmentControllerModule.Interface:
    
    override val model: EnvironmentModelModule.EnvironmentModel = EnvironmentModelImpl(City("Rome"))
    override val view: EnvironmentViewModule.EnvironmentView = EnvironmentViewImpl(simulationView, baseView)
    override val controller: EnvironmentControllerModule.EnvironmentController = EnvironmentControllerImpl()
    
    controller.startSimulation()
