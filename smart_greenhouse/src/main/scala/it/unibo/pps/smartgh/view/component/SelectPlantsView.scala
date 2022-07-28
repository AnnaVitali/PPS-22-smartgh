package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.plants.{PlantSelector, PlantsSelectorController}
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.layout.{HBox, VBox}
import javafx.scene.control.{CheckBox, Label}
import javafx.scene.layout.BorderPane

/** A trait that represents the select plants scene of the application. */
trait SelectPlantsView extends ViewComponent[BorderPane]:
  def showSelectablePlants(selectablePlantList: List[String]): Unit
  def updateSelectedPlant(selectedPlantList: List[String]): Unit

/** Object that can be used to create new instances of [[SelectPlantsView]]. */
object SelectPlantsView:

  /** Creates a new [[SelectPlantsView]] component.
    * @return
    *   a new instance of [[SelectPlantsView]]
    */
  def apply(): SelectPlantsView =
    SelectPlantsViewImpl()

  private class SelectPlantsViewImpl()
      extends AbstractViewComponent[BorderPane]("select_plants.fxml")
      with SelectPlantsView:

    override val component: BorderPane = loader.load[BorderPane]
    private val controller: PlantsSelectorController = PlantsSelectorController()

    @FXML
    var selectablePlantsBox: VBox = _

    @FXML
    var selectedPlantsBox: VBox = _

    @FXML
    var selectYourPlantLabel: Label = _

    @FXML
    var plantsSelectedLabel: Label = _

    @FXML
    var countLabel: Label = _

    selectYourPlantLabel.setText("Select your plants:")
    plantsSelectedLabel.setText("Plants selected:")
    countLabel.setText("Count:")
    controller.view = this
    controller.configureAvailablePlants()

    override def showSelectablePlants(selectablePlantList: List[String]): Unit =
      selectablePlantList.foreach(s => selectablePlantsBox.getChildren().add(new CheckBox(s)))

    override def updateSelectedPlant(selectedPlantList: List[String]): Unit = ???
