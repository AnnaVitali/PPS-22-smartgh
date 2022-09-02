package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.LoadingPlantControllerModule
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{Label, ProgressIndicator}
import javafx.scene.layout.{BorderPane, VBox}

import scala.language.postfixOps

/** Object that encloses the view module for loading the plant data. */
object LoadingPlantViewModule:

  /** A trait that represents the loading plant data scene of the application. */
  trait LoadingPlantView extends ViewComponent[VBox] with ContiguousSceneView[BorderPane]:

    /** Method that increments the progress indicator in the view.
      * @param increment
      *   the increment that you want to apply.
      */
    def incrementProgressIndicator(increment: Double): Unit

  /** Trait that represents the provider of the view, for loading the plant data. */
  trait Provider:
    /** The loading plant view. */
    val loadingPlantView: LoadingPlantView

  /** Requirements for the [[LoadingPlantView]] */
  type Requirements = LoadingPlantControllerModule.Provider with SimulationMVC.Provider

  /** Trait that represents the components of the view for loading the plant data. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[LoadingPlantView]] implementation. */
    class LoadingPlantViewImpl() extends AbstractViewComponent[VBox]("loading_data.fxml") with LoadingPlantView:

      //noinspection VarCouldBeVal
      @FXML
      protected var textLabel: Label = _

      //noinspection VarCouldBeVal
      @FXML
      protected var progressIndicator: ProgressIndicator = _

      textLabel.setText("Loading of plant data in progress, wait a few moments")
      simulationMVC.simulationView.changeSceneButtonBehaviour(visibility = false)

      override def incrementProgressIndicator(increment: Double): Unit =
        Platform.runLater(() => progressIndicator.setProgress(increment))

      override def setNewScene(): Unit =
        Platform.runLater(() => loadingPlantController.beforeNextScene())

      override def moveToNextScene(nextView: ViewComponent[BorderPane]): Unit =
        Platform.runLater(() => simulationMVC.simulationView.changeView(nextView))

  /** Trait that encloses the view for loading the plant data. */
  trait Interface extends Provider with Component:
    self: Requirements =>
