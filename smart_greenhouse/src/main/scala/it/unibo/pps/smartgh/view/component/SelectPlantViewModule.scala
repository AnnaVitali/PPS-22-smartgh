package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.PlantSelectorControllerModule
import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.event.{ActionEvent, EventHandler}
import javafx.fxml.FXML
import javafx.scene.layout.{HBox, VBox}
import javafx.scene.control.{CheckBox, Label}
import javafx.scene.layout.BorderPane
import cats.syntax.eq.catsSyntaxEq
import it.unibo.pps.smartgh.model.plants.{PlantSelectorModelModule, UploadPlants}

import scala.jdk.javaapi.CollectionConverters.asJavaCollection

object SelectPlantViewModule:
  /** A trait that represents the select plants scene of the application. */
  trait View extends ViewComponent[BorderPane]:

    /** Method that shows the plant that can be selected.
      * @param selectablePlantList
      *   the [[List]] of selectable plants name.
      */
    def showSelectablePlants(selectablePlantList: List[String]): Unit

    /** * Method to update the view of the selected plants.
      * @param selectedPlant
      *   the plant that has been selected by the user.
      */
    def updateSelectedPlant(selectedPlant: String): Unit

    /** Method to update the view of the selected plants.
      * @param deselectedPlant
      *   the plant that has ben deselected by the user.
      */
    def updateDeselectedPlant(deselectedPlant: String): Unit

  trait Provider:
    var view: View
  type Requirments = PlantSelectorControllerModule.Provider

  trait Component:
    context: Requirments =>
    class SelectPlantViewImpl(private val simulationView: SimulationView, private val baseView: BaseView)
        extends AbstractViewComponent[BorderPane]("select_plants.fxml")
        with View:

      given Conversion[Int, String] = _.toString

      override val component: BorderPane = loader.load[BorderPane]
      private val selectYourPlantText = "Select your plants:"
      private val plantsSelectedText = "Plants selected:"
      private var plantsCount = 0
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

      baseView.changeSceneButton.setText("Start simulation")
      baseView.changeSceneButton.setOnMouseClicked { _ =>
        //todo
        simulationView.changeView(GreenHouseGlobalView(simulationView, baseView))
      }

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
        plantSelectedLabel = plantSelectedLabel.filter(p => !(p.getText === deselectedPlant))
        selectedPlantsBox.getChildren.removeIf(!plantSelectedLabel.contains(_))
        decrementNumberPlantsSelected()

      private def addEventHandlerToCheckBoxes(checkBoxList: List[CheckBox]): Unit =
        checkBoxList.foreach(_.setOnAction { e =>
          val checkBox = e.getSource.asInstanceOf[CheckBox]
          if checkBox.isSelected then context.controller.notifySelectedPlant(checkBox.getText)
          else context.controller.notifyDeselectedPlant(checkBox.getText)
        })

      private def incrementNumberPlantsSelected(): Unit =
        plantsCount = plantsCount + 1
        numberPlantsSelectedLabel.setText(plantsCount)

      private def decrementNumberPlantsSelected(): Unit =
        plantsCount = plantsCount - 1
        numberPlantsSelectedLabel.setText(plantsCount)

  trait Interface extends Provider with Component:
    self: Requirments =>
