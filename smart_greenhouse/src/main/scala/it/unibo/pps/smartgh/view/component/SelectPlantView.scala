package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.PlantSelectorController
import it.unibo.pps.smartgh.model.plants.PlantSelector
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.event.{ActionEvent, EventHandler}
import javafx.fxml.FXML
import javafx.scene.layout.{HBox, VBox}
import javafx.scene.control.{CheckBox, Label}
import javafx.scene.layout.BorderPane

import scala.jdk.javaapi.CollectionConverters.asJavaCollection

/** A trait that represents the select plants scene of the application. */
trait SelectPlantView extends ViewComponent[BorderPane]:
  /** Method that shows the plant that can be selected
    * @param selectablePlantList
    *   the [[List]] of selectable plants name
    */
  def showSelectablePlants(selectablePlantList: List[String]): Unit

  /** * Method to update the view of the selected plants
    * @param selectedPlant
    *   the plant that has been selected by the user
    */
  def updateSelectedPlant(selectedPlant: String): Unit

  /** Method to update the view of the selected plants
    * @param deselectedPlant
    *   the plant that has ben deselected by the user
    */
  def updateDeselectedPlant(deselectedPlant: String): Unit

/** Object that can be used to create new instances of [[SelectPlantView]]. */
object SelectPlantView:

  /** Creates a new [[SelectPlantView]] component.
    *
    * @return
    *   a new instance of [[SelectPlantView]]
    */
  def apply(): SelectPlantView =
    SelectPlantsViewImpl()

  private class SelectPlantsViewImpl()
      extends AbstractViewComponent[BorderPane]("select_plants.fxml")
      with SelectPlantView:

    given Conversion[Int, String] = _.toString

    override val component: BorderPane = loader.load[BorderPane]
    private val controller: PlantSelectorController = PlantSelectorController()
    private val selectYourPlantText = "Select your plants:"
    private val plantsSelectedText = "Plants selected:"
    private var plantsCount = 0;
    private val countText = "Count: "
    private var plantSelectedLabel: List[Label] = List()

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

    @FXML
    var numberPlantsSelectedLabel: Label = _

    selectYourPlantLabel.setText(selectYourPlantText)
    plantsSelectedLabel.setText(plantsSelectedText)
    countLabel.setText(countText)
    numberPlantsSelectedLabel.setText(plantsCount)
    controller.view = this
    controller.configureAvailablePlants()

    override def showSelectablePlants(selectablePlantList: List[String]): Unit =
      val selectablePlantsCheckBoxList = selectablePlantList.map(new CheckBox(_))
      addEventHandlerToCheckBoxes(selectablePlantsCheckBoxList)
      selectablePlantsBox.getChildren.addAll(asJavaCollection(selectablePlantsCheckBoxList))

    override def updateSelectedPlant(selectedPlant: String): Unit =
      val labelToAdd = Label(selectedPlant)
      plantSelectedLabel = plantSelectedLabel :+ labelToAdd
      selectedPlantsBox.getChildren.add(labelToAdd)
      incrementNumberPlantsSelected()

    override def updateDeselectedPlant(deselectedPlant: String): Unit =
      plantSelectedLabel = plantSelectedLabel.filter(!_.getText.equals(deselectedPlant))
      selectedPlantsBox.getChildren.removeIf(!plantSelectedLabel.contains(_))
      decrementNumberPlantsselected()

    private def addEventHandlerToCheckBoxes(checkBoxList: List[CheckBox]): Unit =
      checkBoxList.foreach(_.setOnAction { e =>
        val checkBox = e.getSource.asInstanceOf[CheckBox]
        if checkBox.isSelected then controller.notifySelectedPlant(checkBox.getText)
        else controller.notifyDeselectedPlant(checkBox.getText)
      })

    private def incrementNumberPlantsSelected(): Unit =
      plantsCount = plantsCount + 1
      numberPlantsSelectedLabel.setText(plantsCount)

    private def decrementNumberPlantsselected(): Unit =
      plantsCount = plantsCount - 1
      numberPlantsSelectedLabel.setText(plantsCount)
