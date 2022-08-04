package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.mvc.MVCCitySearcher
import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.geometry.Pos
import javafx.scene.layout.BorderPane
import javafx.scene.control.{Button, Label}

/** A trait that represents the view of the last scene of the simulation. */
trait FinishSimulationView extends ViewComponent[BorderPane]

/** Object that can be used to create a new instance of [[FinishSimulationView]]. */
object FinishSimulationView:

  /** Create a new [[FinishSimulationView]] component.
    * @param simulationView
    *   the [[SimulationView]] of the application.
    * @param baseView
    *   the [[BaseView]] component.
    * @return
    *   a new instance of [[FinishSimulationView]].
    */
  def apply(simulationView: SimulationView, baseView: BaseView): FinishSimulationView =
    FinishSimulationViewImpl(simulationView, baseView)

  private class FinishSimulationViewImpl(private val simulationView: SimulationView, private val baseView: BaseView)
      extends AbstractViewComponent[BorderPane]("finish_simulation.fxml")
      with FinishSimulationView:

    override val component: BorderPane = loader.load[BorderPane]

    @FXML
    var simulationEndedLabel: Label = _

    val startNewSimulationButton: Button = baseView.changeSceneButton

    simulationEndedLabel.setText("Simulation ended!")
//    startNewSimulationButton.setStyle("-fx-background-color: #33cc33")
//    startNewSimulationButton.setOnMouseEntered(_ => startNewSimulationButton.setStyle("-fx-background-color: #5cd65c"))
//    startNewSimulationButton.setOnMouseExited(_ => startNewSimulationButton.setStyle("-fx-background-color: #33cc33"))

    baseView.changeSceneButton.setText("Start a new simulation")
    baseView.changeSceneButton.setOnMouseClicked { _ =>
      //todo
      simulationView.changeView(MVCCitySearcher(simulationView, baseView).citySearcherView)
    }
