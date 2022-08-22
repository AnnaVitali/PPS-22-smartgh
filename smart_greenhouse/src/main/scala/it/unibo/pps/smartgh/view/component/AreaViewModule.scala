package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.AreaControllerModule
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, ScrollPane}
import javafx.scene.layout.VBox

import scala.language.postfixOps

/** Implementation of the [[AreaViewModule]]. */
object AreaViewModule:
  /** A trait that represents the green house division view of the application. */
  trait AreaView extends ViewComponent[VBox] with ContiguousSceneView[ScrollPane]:
    /** Draw the area.
      * @param plant
      *   name of the [[Plant]] that grows in the area
      * @param statusColor
      *   color of the status of the area
      * @param par
      *   parameters of the area
      */
    def paintArea(plant: String, statusColor: String, par: Map[String, Double]): Unit

    /** Set the [[BaseView]]. */
    var baseView: BaseView

  /** A trait for defining the view instance. */
  trait Provider:
    /** The area's view. */
    val areaView: AreaView

  /** The requirements for the [[AreaView]]. */
  type Requirements = AreaControllerModule.Provider

  /** A trait that represents the greenhouse division view component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the greenhouse division view.
      * @param simulationView
      *   instance of the [[SimulationView]]
      */
    class AreaViewImpl(simulationView: SimulationView) extends AbstractViewComponent[VBox]("area.fxml") with AreaView:

      override val component: VBox = loader.load[VBox]
      override var baseView: BaseView = _

      @FXML
      var params: VBox = _
      @FXML
      var rect: VBox = _
      @FXML
      var areaBt: Button = _
      @FXML
      var plant: Label = _

      override def paintArea(plantName: String, statusColor: String, par: Map[String, Double]): Unit =
        Platform.runLater(() =>
          plant.setText(plantName)
          params.getChildren.clear()
          par foreach ((k, v) =>
            val l = new Label(s"$k : $v")
            l.setMaxWidth(170)
            l.setMinWidth(170)
            l.getStyleClass.setAll(statusColor)
            params.getChildren.add(l)
          )

          areaBt.getStyleClass.setAll(statusColor + "State")

          areaBt.getGraphic
            .asInstanceOf[VBox]
            .getChildren
            .forEach(c => c.asInstanceOf[Label].getStyleClass.setAll(statusColor))

          areaBt.setOnMouseClicked { _ =>
            setNewScene()
          }

          rect.setStyle("-fx-border-color: #000000")
        )

      override def moveToNextScene(component: ViewComponent[ScrollPane]): Unit =
        simulationView.changeView(component)

      override def setNewScene(): Unit =
        areaController.instantiateNextSceneMVC(baseView)

  /** Trait that combine provider and component for area view. */
  trait Interface extends Provider with Component:
    self: Requirements =>
