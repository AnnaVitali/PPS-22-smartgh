package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.LoadingPlantControllerModule
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule.EnvironmentView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{Label, ProgressIndicator}
import javafx.scene.layout.VBox

import scala.language.postfixOps

object LoadingPlantViewModule:

  trait LoadingPlantView extends ViewComponent[VBox]:

    def incrementProgressIndicator(increment: Double): Unit

    def setupNextScene(): Unit

    def moveToNextScene(environmentView: EnvironmentView): Unit

  trait Provider:
    val loadingPlantView: LoadingPlantView

  type Requirements = LoadingPlantControllerModule.Provider

  trait Component:
    context: Requirements =>

    class LoadingPlantViewImpl(simulationView: SimulationView, private val baseView: BaseView)
        extends AbstractViewComponent[VBox]("loading_data.fxml")
        with LoadingPlantView:

      override val component: VBox = loader.load[VBox]

      @FXML
      var textLabel: Label = _

      @FXML
      var progressIndicator: ProgressIndicator = _

      textLabel.setText("loading of plant data in progress, wait a few moments")
      baseView.changeSceneButton.setVisible(false)

      override def incrementProgressIndicator(increment: Double): Unit =
        Platform.runLater(() => progressIndicator.setProgress(increment))

      override def setupNextScene(): Unit =
        Platform.runLater(() => loadingPlantController.instantiateNextSceneMVC(baseView))

      override def moveToNextScene(environmentView: EnvironmentView): Unit =
        Platform.runLater(() =>
          baseView.changeSceneButton.setVisible(true)
          simulationView.changeView(environmentView)
        )

  trait Interface extends Provider with Component:
    self: Requirements =>
