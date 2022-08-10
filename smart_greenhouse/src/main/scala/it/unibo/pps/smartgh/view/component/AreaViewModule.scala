package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.AreaControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.scene.control.Label

import scala.language.postfixOps

/** Implementation of the [[AreaViewModule]]. */
object AreaViewModule:
  /** A trait that represents the green house division view of the application. */
  trait AreaView extends ViewComponent[VBox]:
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
  type Requirements = AreaControllerModule.Provider

  /** A trait that represents the greenhouse division view component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the greenhouse division view. */
    class AreaViewImpl() extends AbstractViewComponent[VBox]("area.fxml") with AreaView:

      override val component: VBox = loader.load[VBox]

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
          par foreach ((k, v) => params.getChildren.add(new Label(s"$k : $v")))

          areaBt.setStyle("-fx-background-color: " + statusColor)

          areaBt.setOnMouseEntered { _ =>
            areaBt.getGraphic
              .asInstanceOf[VBox]
              .getChildren
              .forEach(c =>
                c.asInstanceOf[Label].setStyle("-fx-text-fill: #ffffff ; -fx-background-color: " + statusColor)
              )
          }

          areaBt.setOnMouseExited { _ =>
            areaBt.getGraphic
              .asInstanceOf[VBox]
              .getChildren
              .forEach(c =>
                c.asInstanceOf[Label].setStyle("-fx-text-fill: #000000 ; -fx-background-color: " + statusColor)
              )
          }
          areaBt.setOnMouseClicked { _ =>
            areaController.openArea()
          }

          rect.setStyle("-fx-border-color: #000000")
        )

  /** Trait that combine provider and component for area view. */
  trait Interface extends Provider with Component:
    self: Requirements =>
