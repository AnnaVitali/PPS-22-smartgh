package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.{AreaControllerModule, AreaDetailsControllerModule}
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.component.AreaLuminosityMVC
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule.SelectPlantView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import it.unibo.pps.smartgh.view.component.AreaLuminosityViewModule
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.{Label, ScrollPane, Separator}
import javafx.scene.layout.VBox

object AreaDetailsViewModule:

  trait AreaDetailsView extends ViewComponent[ScrollPane]:
    def moveToNextScene[A <: Parent](viewComponent: ViewComponent[A]): Unit
    def initializeParameters(simulationMVC: SimulationMVCImpl, areaModel: AreaModel): Unit

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
      var parametersVbox: VBox = _

      Platform.runLater(() => baseView.changeSceneButton.setText("Back"))
      baseView.changeSceneButton.setOnMouseClicked { _ => /*todo*/ }
      areaDetailsController.initializeView()

      override def initializeParameters(simulationMVC: SimulationMVCImpl, areaModel: AreaModel): Unit =
        parametersVbox.getChildren.add(AreaLuminosityMVC(simulationMVC, baseView, areaModel).areaLuminosityView)
        parametersVbox.getChildren.add(Separator())

      override def moveToNextScene[A <: Parent](viewComponent: ViewComponent[A]): Unit =
        simulationView.changeView(viewComponent)

  trait Interface extends Provider with Component:
    self: Requirements =>
