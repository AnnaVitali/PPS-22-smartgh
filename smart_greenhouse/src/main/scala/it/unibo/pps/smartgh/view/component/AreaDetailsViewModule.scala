package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.{AreaControllerModule, AreaDetailsControllerModule}
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule.SelectPlantView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.VBox

object AreaDetailsViewModule:

  trait AreaDetailsView extends ViewComponent[VBox]:
    def moveToNextScene[A <: Parent](viewComponent: ViewComponent[A]): Unit

  trait Provider:
    val areaDetailsViewModule: AreaDetailsView

  type Requirements = AreaDetailsControllerModule.Provider

  trait Component:
    context: Requirements =>

    class AreaDetailsViewImpl(private val simulationView: SimulationView, private val baseView: BaseView)
        extends AbstractViewComponent[VBox]("area_details.fxml")
        with AreaDetailsView:

      override val component: VBox = loader.load[VBox]

      @FXML
      var plantNameLabel: Label = _

      @FXML
      var plantDescriptionLabel: Label = _

      @FXML
      var parametersVbox: VBox = _

      Platform.runLater(() => baseView.changeSceneButton.setText("Back"))
      baseView.changeSceneButton.setOnMouseClicked { _ => /*todo*/ }

      override def moveToNextScene[A <: Parent](viewComponent: ViewComponent[A]): Unit =
        simulationView.changeView(viewComponent)

  trait Interface extends Provider with Component:
    self: Requirements =>
