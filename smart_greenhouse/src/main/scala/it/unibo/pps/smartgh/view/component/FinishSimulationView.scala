package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import javafx.scene.control.{Button, Label}

/** A trait that represents the view of the last scene of the simulation. */
trait FinishSimulationView extends ViewComponent[BorderPane]

/** Object that can be used to create a new instance of [[FinishSimulationView]]. */
object FinishSimulationView:

  /** Create a new [[FinishSimulationView]] component.
    * @return
    *   a new instance of [[FinishSimulationView]]
    */
  def apply(): FinishSimulationView = FinishSimulationViewImpl()

  private class FinishSimulationViewImpl()
      extends AbstractViewComponent[BorderPane]("finish_simulation.fxml")
      with FinishSimulationView:

    override val component: BorderPane = loader.load[BorderPane]

    @FXML
    var simulationEndedLabel: Label = _

    @FXML
    var startNewSimulationButton: Button = _

    simulationEndedLabel.setText("Simulation ended!")
    startNewSimulationButton.setText("Start a new simulation")
    startNewSimulationButton.setStyle("-fx-background-color: #33cc33")
    startNewSimulationButton.setOnMouseEntered(_ => startNewSimulationButton.setStyle("-fx-background-color: #5cd65c"))
    startNewSimulationButton.setOnMouseExited(_ => startNewSimulationButton.setStyle("-fx-background-color: #33cc33"))
