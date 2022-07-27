package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import javafx.scene.control.{Button, Label}

trait FinishSimulationView extends ViewComponent[BorderPane]

object FinishSimulationView:

  def apply(): FinishSimulationView = FinishSimulationViewImpl().build()

  private class FinishSimulationViewImpl()
      extends AbstractViewComponent[BorderPane]("finish_simulation.fxml")
      with FinishSimulationView:

    override val component: BorderPane = loader.load[BorderPane]

    @FXML
    var simulationEndedLabel: Label = _

    @FXML
    var startNewSimulationButton: Button = _

    def build(): FinishSimulationViewImpl =
      simulationEndedLabel.setText("Simulation ended!")
      startNewSimulationButton.setText("Start a new simulation")
      startNewSimulationButton.setStyle("-fx-background-color: #33cc33")
      setButtonMouseEvent()
      this

    private def setButtonMouseEvent(): Unit =
      startNewSimulationButton.setOnMouseEntered(_ =>
        startNewSimulationButton.setStyle("-fx-background-color: #5cd65c")
      )
      startNewSimulationButton.setOnMouseExited(_ => startNewSimulationButton.setStyle("-fx-background-color: #33cc33"))
