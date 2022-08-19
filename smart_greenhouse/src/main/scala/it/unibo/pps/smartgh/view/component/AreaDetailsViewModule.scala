package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.{AreaControllerModule, AreaDetailsControllerModule}
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.component.areaParameters.{
  AreaAirHumidityMVC,
  AreaLuminosityMVC,
  AreaSoilMoistureMVC,
  AreaTemperatureMVC
}
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule.SelectPlantView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import it.unibo.pps.smartgh.view.component.areaParameters.AreaLuminosityViewModule
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.{Label, ScrollPane, Separator}
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox

object AreaDetailsViewModule:

  trait AreaDetailsView extends ViewComponent[ScrollPane]:
    def moveToNextScene[A <: Parent](viewComponent: ViewComponent[A]): Unit
    def initializeParameters(
        simulationMVC: SimulationMVCImpl,
        areaModel: AreaModel,
        updateStateMessage: (String, Boolean) => Unit
    ): Unit
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
      var alarmLabel: Label = _

      Platform.runLater(() => baseView.changeSceneButton.setText("Back"))
      baseView.changeSceneButton.setOnMouseClicked { _ => /*todo*/ }

      override def initializeParameters(
          simulationMVC: SimulationMVCImpl,
          areaModel: AreaModel,
          updateStateMessage: (String, Boolean) => Unit
      ): Unit =
        //todo: automatizzare la creazione delle tipologie di parametri
        parametersVbox.getChildren.add(AreaLuminosityMVC(simulationMVC, baseView, areaModel).areaLuminosityView)
        parametersVbox.getChildren.add(Separator())
        parametersVbox.getChildren.add(AreaTemperatureMVC(simulationMVC, areaModel).areaTemperatureView)
        parametersVbox.getChildren.add(Separator())
        parametersVbox.getChildren.add(AreaAirHumidityMVC(simulationMVC, areaModel).areaAirHumidityView)
        parametersVbox.getChildren.add(Separator())
        parametersVbox.getChildren.add(
          AreaSoilMoistureMVC(simulationMVC, areaModel, updateStateMessage).areaSoilMoistureView
        )
        parametersVbox.getChildren.add(Separator())

      override def moveToNextScene[A <: Parent](viewComponent: ViewComponent[A]): Unit =
        simulationView.changeView(viewComponent)

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

      override def updateStateMessages(messages: String): Unit = Platform.runLater(() => alarmLabel.setText(messages))

  trait Interface extends Provider with Component:
    self: Requirements =>
