package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.AreaDetailsControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.component.areaParameters.{
  AreaAirHumidityMVC,
  AreaLuminosityMVC,
  AreaSoilMoistureMVC,
  AreaTemperatureMVC
}
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule.SelectPlantView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.{Label, ScrollPane, Separator}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{BorderPane, Pane, VBox}

import java.net.URL

object AreaDetailsViewModule:

  trait AreaDetailsView extends ViewComponent[ScrollPane] with ContiguousSceneView[BorderPane]:
    def initializeParameters(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): Unit
    def updatePlantInformation(name: String, description: String, imageUrl: String): Unit
    def updateTime(time: String): Unit
    def updateState(state: String): Unit
    def updateStateMessages(messages: String): Unit

  trait Provider:
    val areaDetailsView: AreaDetailsView

  type Requirements = AreaDetailsControllerModule.Provider

  trait Component:
    context: Requirements =>

    class AreaDetailsViewImpl(private val simulationView: SimulationView, private val baseView: BaseView)
        extends AbstractViewComponent[ScrollPane]("area_details.fxml")
        with AreaDetailsView:

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

      Platform.runLater(() => baseView.changeSceneButton.setText("Back"))
      baseView.changeSceneButton.setOnMouseClicked(_ => setNewScene())
      alarmPane.managedProperty().bind(alarmPane.visibleProperty())

      override def initializeParameters(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): Unit =
        //todo: automatizzare la creazione delle tipologie di parametri
        parametersVbox.getChildren.add(AreaLuminosityMVC(areaModel, updateStateMessage).areaLuminosityView)
        parametersVbox.getChildren.add(Separator())
        parametersVbox.getChildren.add(AreaTemperatureMVC(areaModel, updateStateMessage).areaTemperatureView)
        parametersVbox.getChildren.add(Separator())
        parametersVbox.getChildren.add(AreaAirHumidityMVC(areaModel, updateStateMessage).areaAirHumidityView)
        parametersVbox.getChildren.add(Separator())
        parametersVbox.getChildren.add(AreaSoilMoistureMVC(areaModel, updateStateMessage).areaSoilMoistureView)
        parametersVbox.getChildren.add(Separator())

      override def updatePlantInformation(name: String, description: String, imageUrl: String): Unit =
        Platform.runLater { () =>
          plantNameLabel.setText(name.capitalize)
          plantDescriptionLabel.setText(description)
          plantImage.setImage(Image(imageUrl))
        }

      override def updateTime(time: String): Unit = Platform.runLater(() => timerLabel.setText(time))

      override def updateState(state: String): Unit = Platform.runLater { () =>
        statusLabel.setText(state)
        statusLabel.getStyleClass.setAll(state)
      }

      override def updateStateMessages(messages: String): Unit =
        Platform.runLater { () =>
          alarmPane.setVisible(!messages.isBlank)
          alarmLabel.setText(messages)
        }

      override def moveToNextScene(component: ViewComponent[BorderPane]): Unit =
        simulationView.changeView(component)

      override def setNewScene(): Unit =
        areaDetailsController.instantiateNextSceneMVC(baseView)

  trait Interface extends Provider with Component:
    self: Requirements =>
