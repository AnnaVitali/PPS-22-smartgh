package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.SimulationControllerModule
import it.unibo.pps.smartgh.controller.SimulationControllerModule.SimulationController
import it.unibo.pps.smartgh.mvc.component.SelectCityMVC
import it.unibo.pps.smartgh.view.SimulationViewModule
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import javafx.stage.Stage

/** A trait that represents the MVC component for the simulation. */
trait SimulationMVC extends SimulationViewModule.Interface with SimulationControllerModule.Interface

/** Object that encapsulates the model view and controller module for the simulation. */
object SimulationMVC:

  /** Trait that represents the provider of the [[SimulationMVC]]. */
  trait Provider:
    /** Return the implementation of the simulation MVC. */
    val simulationMVC: SimulationMVC

  /** Create a new [[SimulationMVC]].
    * @param stage
    *   the [[Stage]] of the application.
    * @return
    *   a new instance of [[SimulationMVC]].
    */
  def apply(stage: Stage): SimulationMVC = SimulationMVCImpl(stage)

  private class SimulationMVCImpl(stage: Stage) extends SimulationMVC:

    override val simulationController: SimulationController = SimulationControllerImpl()
    override val simulationView: SimulationView = SimulationViewImpl(stage)

  /** Trait that encloses the MVC of the simulation. */
  trait Interface extends Provider
