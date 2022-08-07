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
import javafx.application.Platform
import it.unibo.pps.smartgh.mvc.EnvironmentMVC

import scala.jdk.javaapi.CollectionConverters.asJavaCollection
import scala.language.postfixOps

/** Object that encloses the view module for the plant selection. */
object SelectPlantViewModule:

  /** A trait that represents the select plants scene of the application. */
  trait SelectPlantView extends ViewComponent[BorderPane]:

    /** Method that shows the plant that can be selected.
      * @param selectablePlantList
      *   the [[List]] of selectable plants name.
      */
    def showSelectablePlants(selectablePlantList: List[String]): Unit

    /** * Method to update the view of the selected plants.
      * @param selectedPlant
      *   the plant that has been selected by the user.
      */
    def updateSelectedPlant(selectedPlantList: List[String]): Unit

    /** Method that asks the view to move to the next Scene. */
    def moveToTheNextScene(): Unit

    /** Method that requires to the view to show an error message.
      * @param message
      *   the error message that needs to be shown to the user.
      */
    def showErrorMessage(message: String): Unit

  /** Trait that represents the provider of the view for the plant selection. */
  trait Provider:
    val selectPlantView: SelectPlantView
  type Requirments = PlantSelectorControllerModule.Provider

  /** Trait that represents the components of the view for the plant selection. */
  trait Component:
    context: Requirments =>

    /** Class that contains the [[SelectPlantView]] implementation.
      * @param simulationView
      *   the root view of the application.
      * @param baseView
      *   the view in which the [[SelectPlantView]] is enclosed.
      */
    class SelectPlantViewImpl(private val simulationView: SimulationView, private val baseView: BaseView)
        extends AbstractViewComponent[BorderPane]("select_plants.fxml")
        with SelectPlantView:

      given Conversion[Int, String] = _.toString

      override val component: BorderPane = loader.load[BorderPane]
      private val selectYourPlantText = "Select your plants:"
      private val plantsSelectedText = "Plants selected:"
      private val countText = "Count: "

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

      @FXML
      var errorLabel: Label = _

      selectYourPlantLabel.setText(selectYourPlantText)
      plantsSelectedLabel.setText(plantsSelectedText)
      countLabel.setText(countText)
      numberPlantsSelectedLabel.setText(0)

      baseView.changeSceneButton.setText("Start simulation")
      baseView.changeSceneButton.setOnMouseClicked { _ =>
        context.plantSelectorController.notifyStartSimulationClicked()
      }

      override def showSelectablePlants(selectablePlantList: List[String]): Unit =
        val selectablePlantsCheckBoxList = selectablePlantList.map(new CheckBox(_))
        addEventHandlerToCheckBoxes(selectablePlantsCheckBoxList)
        selectablePlantsBox.getChildren.addAll(asJavaCollection(selectablePlantsCheckBoxList))

      override def updateSelectedPlant(selectedPlants: List[String]): Unit =
        Platform.runLater(() =>
          selectedPlantsBox.getChildren.setAll(asJavaCollection(selectedPlants.map(new Label(_))))
          numberPlantsSelectedLabel.setText(selectedPlants.length)
        )

      private def addEventHandlerToCheckBoxes(checkBoxList: List[CheckBox]): Unit =
        checkBoxList.foreach(_.setOnAction { e =>
          Platform.runLater(() =>
            val checkBox = e.getSource.asInstanceOf[CheckBox]
            if checkBox.isSelected then context.plantSelectorController.notifySelectedPlant(checkBox.getText)
            else context.plantSelectorController.notifyDeselectedPlant(checkBox.getText)
          )
        })

      override def moveToTheNextScene(): Unit =
        val environmentMVC = EnvironmentMVC(simulationView, baseView)
        simulationView.changeView(environmentMVC.environmentView)

      override def showErrorMessage(message: String): Unit =
        errorLabel.setText(message)

  /** Trait that encloses the view for the plant selection. */
  trait Interface extends Provider with Component:
    self: Requirments =>
