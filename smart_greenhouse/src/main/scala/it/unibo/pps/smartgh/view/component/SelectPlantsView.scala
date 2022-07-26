package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.layout.{HBox, VBox}
import javafx.scene.control.Label

trait SelectPlantsView extends ViewComponent[HBox]

object SelectPlantsView:

  def apply(): SelectPlantsView =
    SelectPlantsViewImpl()

  private class SelectPlantsViewImpl() extends AbstractViewComponent[HBox]("select_plants.fxml") with SelectPlantsView:

    override val component: HBox = loader.load[HBox]

    @FXML
    var selectablePlants: VBox = _

    @FXML
    var selectedPlants: VBox = _

    @FXML
    var selectYourPlantLabel: Label = _

    @FXML
    var plantsSelectedLabel: Label = _


    def build(): this =
      selectYourPlantLabel.setText("Select your plants:")
      plantsSelectedLabel.setText("Plants selected:")

