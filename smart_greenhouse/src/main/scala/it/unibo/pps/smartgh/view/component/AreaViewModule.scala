package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.AreaControllerModule
import it.unibo.pps.smartgh.mvc.SimulationMVC
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

  /** A trait for defining the view instance. */
  trait Provider:
    /** The area's view. */
    val areaView: AreaView

  /** The requirements for the [[AreaView]]. */
  type Requirements = AreaControllerModule.Provider with SimulationMVC.Provider

  /** A trait that represents the greenhouse division view component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the greenhouse division view. */
    class AreaViewImpl() extends AbstractViewComponent[VBox]("area.fxml") with AreaView:

      @FXML
      protected var params: VBox = _

      @FXML
      protected var rect: VBox = _

      @FXML
      protected var areaBt: Button = _

      //noinspection VarCouldBeVal
      @FXML
      private var plant: Label = _

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

      override def moveToNextScene(nextView: ViewComponent[ScrollPane]): Unit =
        simulationMVC.simulationView.changeView(nextView)

      override def setNewScene(): Unit =
        areaController.beforeNextScene()

  /** Trait that combine provider and component for area view. */
  trait Interface extends Provider with Component:
    self: Requirements =>
