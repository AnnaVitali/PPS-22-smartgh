package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.SimulationControllerModule
import it.unibo.pps.smartgh.view.SimulationViewModule
import javafx.stage.Stage

/** Object that incapsulate the model view and controller module for the simulation. */
object SimulationMVC:

  /** Create a new [[SimulationMVCImpl]].
    * @param stage
    *   the [[Stage]] of the application.
    * @return
    *   a new instance of [[SimulationMVCImpl]].
    */
  def apply(stage: Stage): SimulationMVCImpl = SimulationMVCImpl(stage)

  /** Implementation of the simulation MVC.
    * @param stage
    *   the [[Stage]] of the application.
    */
  class SimulationMVCImpl(stage: Stage)
      extends SimulationViewModule.Interface
      with SimulationControllerModule.Interface:

    override val simulationController: SimulationControllerModule.SimulationController =
      SimulationControllerImpl()
    override val simulationView: SimulationViewModule.SimulationView = SimulationViewImpl(stage)

    simulationView.start(this)
