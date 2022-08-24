package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.AreaDetailsControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.areaParameters.{
  AreaAirHumidityMVC,
  AreaLuminosityMVC,
  AreaSoilMoistureMVC,
  AreaTemperatureMVC
}
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule.SelectPlantView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.AreaParametersView
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.{Label, ProgressIndicator, ScrollPane, Separator}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{BorderPane, GridPane, Pane, VBox}
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import java.net.URL

/** Object that encloses the view module for the area details. */
object AreaDetailsViewModule:

  /** Trait that represents the area details view. */
  trait AreaDetailsView extends ViewComponent[ScrollPane] with ContiguousSceneView[BorderPane]:

    /** Initialize parameters.
      * @param parameters
      *   a sequence of parameters to show.
      */
    def initializeParameters(parameters: Seq[AreaParametersView]): Unit

    /** Updating the plant information.
      * @param name
      *   the plant name
      * @param description
      *   the plant description
      * @param imageUrl
      *   the plant image url
      */
    def updatePlantInformation(name: String, description: String, imageUrl: String): Unit

    /** Update the time value information to display.
      * @param time
      *   the time value
      */
    def updateTime(time: String): Unit

    /** Update the area state information.
      * @param state
      *   the area state
      */
    def updateState(state: String): Unit

    /** Update the state messages.
      * @param messages
      *   the message to display
      */
    def updateStateMessages(messages: String): Unit

  /** Trait that represents the provider of the area details. */
  trait Provider:

    /** The view of area details. */
    val areaDetailsView: AreaDetailsView

  /** The view requirements. */
  type Requirements = AreaDetailsControllerModule.Provider with SimulationMVC.Provider

  /** Trait that represents the components of the view for the area details. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaDetailsView]] implementation. */
    class AreaDetailsViewImpl() extends AbstractViewComponent[ScrollPane]("area_details.fxml") with AreaDetailsView:

      override val component: ScrollPane = loader.load[ScrollPane]

      @FXML
      var plantNameLabel: Label = _

      @FXML
      var plantDescriptionLabel: Label = _

      @FXML
      var plantImage: ImageView = _

      @FXML
      var timerLabel: Label = _

      @FXML
      var statusLabel: Label = _

      @FXML
      var parametersVbox: VBox = _

      @FXML
      var alarmPane: Pane = _

      @FXML
      var alarmLabel: Label = _

      @FXML
      var loadingImg: ProgressIndicator = _

      simulationMVC.simulationView.changeSceneButtonBehaviour("Back", _ => setNewScene())
      simulationMVC.simulationView.changeSceneButtonStyle("defaultButton")
      alarmPane.managedProperty().bind(alarmPane.visibleProperty())
      loadingImg.setVisible(true)

      override def initializeParameters(parameters: Seq[AreaParametersView]): Unit =
        parameters.foreach { v =>
          parametersVbox.getChildren.add(v)
          parametersVbox.getChildren.add(Separator())
        }

      override def updatePlantInformation(name: String, description: String, imageUrl: String): Unit =
        Platform.runLater { () =>
          plantNameLabel.setText(name)
          plantDescriptionLabel.setText(description)
          Task {
            plantImage.setImage(Image(imageUrl))
            loadingImg.setVisible(false)
          }.executeAsync.runToFuture
        }

      override def updateTime(time: String): Unit = Platform.runLater(() => timerLabel.setText(time))

      override def updateState(state: String): Unit =
        Platform.runLater { () =>
          statusLabel.setText(state)
          statusLabel.getStyleClass.setAll(state)
        }

      override def updateStateMessages(messages: String): Unit =
        Platform.runLater { () =>
          alarmPane.setVisible(!messages.isBlank)
          alarmLabel.setText(messages)
        }

      override def moveToNextScene(nextView: ViewComponent[BorderPane]): Unit =
        simulationMVC.simulationView.changeView(nextView)

      override def setNewScene(): Unit =
        simulationMVC.simulationView.changeSceneButtonStyle("alarmButton")
        areaDetailsController.beforeNextScene()

  /** Trait that encloses the view for area details. */
  trait Interface extends Provider with Component:
    self: Requirements =>
