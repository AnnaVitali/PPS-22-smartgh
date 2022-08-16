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

/** Object that encloses the view module for loading the plant data. */
object LoadingPlantViewModule:

  /** A trait that represents the loading plant data scene of the application. */
  trait LoadingPlantView extends ViewComponent[VBox]:

    /** Method that increments the progress indicator in the view.
      * @param increment
      *   the increment that you want to apply.
      */
    def incrementProgressIndicator(increment: Double): Unit

    /** Method that requires to the view to set up the nex scene of the application. */
    def setupNextScene(): Unit

    /** Method that asks the view to move to the next Scene.
      * @param environmentView
      *   the next scene that needs to be shown.
      */
    def moveToNextScene(environmentView: EnvironmentView): Unit

  /** Trait that represents the provider of the view, for loading the plant data. */
  trait Provider:
    /** The looading plant view. */
    val loadingPlantView: LoadingPlantView

  /** Requirements for the [[LoadingPlantView]] */
  type Requirements = LoadingPlantControllerModule.Provider

  /** Trait that represents the components of the view for loading the plant data. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[LoadingPlantView]] implementation.
      * @param simulationView
      *   he root view of the application
      * @param baseView
      *   the view in which the [[LoadingPlantView]] is enclosed.
      */
    class LoadingPlantViewImpl(simulationView: SimulationView, private val baseView: BaseView)
        extends AbstractViewComponent[VBox]("loading_data.fxml")
        with LoadingPlantView:

      override val component: VBox = loader.load[VBox]

      @FXML
      var textLabel: Label = _

      @FXML
      var progressIndicator: ProgressIndicator = _

      textLabel.setText("Loading of plant data in progress, wait a few moments")
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

  /** Trait that encloses the view for loading the plant data. */
  trait Interface extends Provider with Component:
    self: Requirements =>
