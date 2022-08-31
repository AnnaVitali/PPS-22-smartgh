package it.unibo.pps.smartgh.view.component

import cats.syntax.eq.catsSyntaxEq
import it.unibo.pps.smartgh.controller.component.PlantSelectorControllerModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.{CheckBox, Label}
import javafx.scene.layout.{BorderPane, VBox}
import org.scalactic.TripleEquals.convertToEqualizer

import scala.jdk.javaapi.CollectionConverters.asJavaCollection
import scala.language.postfixOps

/** Object that encloses the view module for the plant selection. */
object SelectPlantViewModule:

  /** A trait that represents the select plants scene of the application. */
  trait SelectPlantView extends ViewComponent[BorderPane] with ContiguousSceneView[VBox]:

    /** Method that shows the plant that can be selected.
      * @param selectablePlantList
      *   the [[List]] of selectable plants name.
      */
    def showSelectablePlants(selectablePlantList: List[String]): Unit

    /** * Method to update the view of the selected plants.
      * @param selectedPlantList
      *   the [[List]] of the plant that has been selected by the user.
      */
    def updateSelectedPlant(selectedPlantList: List[String]): Unit

    /** Method that requires to the view to show an error message.
      * @param message
      *   the error message that needs to be shown to the user.
      */
    def showErrorMessage(message: String): Unit

  /** Trait that represents the provider of the view for the plant selection. */
  trait Provider:
    /** Select plant view. */
    val selectPlantView: SelectPlantView

  /** Requirements for the [[SelectPlantView]] */
  type Requirements = PlantSelectorControllerModule.Provider with SimulationMVC.Provider

  /** Trait that represents the components of the view for the plant selection. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[SelectPlantView]] implementation. */
    class SelectPlantViewImpl() extends AbstractViewComponent[BorderPane]("select_plants.fxml") with SelectPlantView:

      given Conversion[Int, String] = _.toString

      private val selectYourPlantText = "Select your plants:"
      private val plantsSelectedText = "Plants selected:"
      private val countText = "Count: "

      @FXML
      protected var selectablePlantsBox: VBox = _

      //noinspection VarCouldBeVal
      @FXML
      protected var selectedPlantsBox: VBox = _

      //noinspection VarCouldBeVal
      @FXML
      protected var selectYourPlantLabel: Label = _

      //noinspection VarCouldBeVal
      @FXML
      protected var plantsSelectedLabel: Label = _

      //noinspection VarCouldBeVal
      @FXML
      protected var countLabel: Label = _

      //noinspection VarCouldBeVal
      @FXML
      protected var numberPlantsSelectedLabel: Label = _

      //noinspection VarCouldBeVal
      @FXML
      protected var errorLabel: Label = _

      selectYourPlantLabel.setText(selectYourPlantText)
      plantsSelectedLabel.setText(plantsSelectedText)
      countLabel.setText(countText)
      numberPlantsSelectedLabel.setText(0)

      simulationMVC.simulationView.changeSceneButtonBehaviour(
        "Start simulation",
        _ => plantSelectorController.beforeNextScene()
      )

      override def showSelectablePlants(selectablePlantList: List[String]): Unit =
        Platform.runLater(() =>
          //noinspection ScalaUnnecessaryParentheses
          val selectablePlantsCheckBoxList = selectablePlantList.map(new CheckBox(_))
          addEventHandlerToCheckBoxes(selectablePlantsCheckBoxList)
          selectablePlantsBox.getChildren.addAll(asJavaCollection(selectablePlantsCheckBoxList))
        )

      override def updateSelectedPlant(selectedPlants: List[String]): Unit =
        Platform.runLater(() =>
          selectedPlantsBox.getChildren.setAll(asJavaCollection(selectedPlants.map(new Label(_))))
          numberPlantsSelectedLabel.setText(selectedPlants.length)
        )

      private def addEventHandlerToCheckBoxes(checkBoxList: List[CheckBox]): Unit =
        checkBoxList.foreach(_.setOnAction { e =>
          Platform.runLater(() =>
            val checkBox = e.getSource.asInstanceOf[CheckBox]
            if errorLabel.getText !== "" then errorLabel.setText("")
            if checkBox.isSelected then plantSelectorController.notifySelectedPlant(checkBox.getText)
            else plantSelectorController.notifyDeselectedPlant(checkBox.getText)
          )
        })

      override def moveToNextScene(nextView: ViewComponent[VBox]): Unit =
        Platform.runLater(() => simulationMVC.simulationView.changeView(nextView))

      override def setNewScene(): Unit =
        plantSelectorController.beforeNextScene()

      override def showErrorMessage(message: String): Unit =
        Platform.runLater(() => errorLabel.setText(message))

  /** Trait that encloses the view for the plant selection. */
  trait Interface extends Provider with Component:
    self: Requirements =>
