package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.component.SelectCityMVC
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.BorderPane

/** A trait that represents the view of the last scene of the simulation. */
trait FinishSimulationView extends ViewComponent[BorderPane]

/** Object that can be used to create a new instance of [[FinishSimulationView]]. */
object FinishSimulationView:

  /** Create a new [[FinishSimulationView]] component.
    *
    * @param simulationMVC
    *   the root MVC of the application.
    * @return
    *   a new instance of [[FinishSimulationView]].
    */
  def apply(simulationMVC: SimulationMVCImpl): FinishSimulationView =
    FinishSimulationViewImpl(simulationMVC)

  private class FinishSimulationViewImpl(simulationMVC: SimulationMVCImpl)
      extends AbstractViewComponent[BorderPane]("finish_simulation.fxml")
      with FinishSimulationView:

    //noinspection VarCouldBeVal
    @FXML
    protected var simulationEndedLabel: Label = _

    Platform.runLater(() => simulationEndedLabel.setText("Simulation ended!"))
    simulationMVC.simulationView.changeSceneButtonBehaviour(
      "Start a new simulation",
      _ => {
        simulationMVC.simulationController.resetSimulation()
        simulationMVC.simulationView.changeView(SelectCityMVC(simulationMVC).selectCityView)
      }
    )
