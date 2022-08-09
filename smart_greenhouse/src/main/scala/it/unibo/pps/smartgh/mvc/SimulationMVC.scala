package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.SimulationControllerModule
import it.unibo.pps.smartgh.view.SimulationViewModule
import javafx.stage.Stage

object SimulationMVC:

  def apply(stage: Stage): SimulationMVCImpl = SimulationMVCImpl(stage)

  class SimulationMVCImpl(stage: Stage)
      extends SimulationViewModule.Interface
      with SimulationControllerModule.Interface:

    override val simulationController: SimulationControllerModule.SimulationController =
      SimulationControllerImpl()
    override val simulationView: SimulationViewModule.SimulationView = SimulationViewImpl(stage)
    simulationView.start(this)
