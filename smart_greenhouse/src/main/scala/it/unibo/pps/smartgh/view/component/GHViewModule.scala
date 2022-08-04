package it.unibo.pps.smartgh.view.component

import cats.syntax.eq.catsSyntaxEq
import it.unibo.pps.smartgh.controller.GHControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.layout.{GridPane, StackPane, VBox}
import scalafx.geometry.Pos
import scalafx.scene.Cursor.Text
import scalafx.scene.control.Button
import scalafx.scene.paint.Color.*
import scalafx.scene.layout.Pane
import scalafx.scene.shape.Rectangle

import scala.language.postfixOps
/** Implementation of the [[GHViewModule]]. */
object GHViewModule:
  /** A trait that represents the green house division view of the application. */
  trait View extends ViewComponent[VBox]:
    /** Draws the greenhouse division according to the rows and cols.
     * @param rows of the greenhouse grid
     * @param cols of the greenhouse grid
     * @param areas list of areas componing the greenhouse
     * */
    def paintDivision(rows: Int, cols: Int, areas: List[(String, Boolean)]): Unit
    
  /** A trait for defining the view instance.*/
  trait Provider:
    val view: View

  type Requirements = GHControllerModule.Provider
  /** A trait that represents the greenhouse division view component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the greenhouse division view.*/
    class GreenHouseDivisionViewImpl()
      extends AbstractViewComponent[VBox]("ghDivision.fxml")
        with View:
      private val env = GridPane()
      override val component: VBox = loader.load[VBox]

      @FXML
      var ghDivision: VBox = _

      override def paintDivision(rows: Int, cols: Int, areas: List[(String, Boolean)]): Unit =
        Platform.runLater(() =>
          ghDivision.getChildren.clear()
          ghDivision.getChildren.add(env)
          var count = 0
          for
            c <- 0 until cols
            r <- 0 until rows
          do
            val i = count
            val rect = new VBox()

            val params = new VBox()
            params.setId(areas(i)._1 + "_params")
            params.setAlignment(Pos.Center)
            params.getChildren.add(new Label("param 1"))
            params.getChildren.add(new Label("param 2"))
            params.getChildren.add(new Label("param 3"))

            val area = new Button
            area.setId(areas(i)._1 + "_bt")
            area.setGraphic(params)
            val color = if areas(i)._2 then "#33cc33" else "#cc3333"
            area.style = "-fx-background-color: " + color
            area.onMouseEntered = _ =>
              area.graphic
                .get()
                .asInstanceOf[VBox]
                .getChildren
                .forEach(c => c.asInstanceOf[Label].setStyle("-fx-text-fill: #ffffff ; -fx-background-color: " + color))
            area.onMouseExited = _ =>
              area.graphic
                .get()
                .asInstanceOf[VBox]
                .getChildren
                .forEach(c => c.asInstanceOf[Label].setStyle("-fx-text-fill: #000000 ; -fx-background-color: " + color))
            area.onMouseClicked = _ =>
              context.controller.openArea(areas(i))
              paintDivision(rows, cols, areas.map(a => if a === areas(i) then (a._1, !a._2) else a))

            val label = Label()
            label.setText(areas(i)._1)

            rect.getChildren.add(area)
            rect.getChildren.add(label)
            rect.setSpacing(5)
            rect.setAlignment(Pos.Center)
            rect.setStyle("-fx-border-color: #000000")
            env.add(rect, c, r)
            count = count + 1

            env.setHgap(5)
            env.setVgap(5)
        )

  /** Trait that combine provider and component for greenhouse division view.*/
  trait Interface extends Provider with Component:
    self: Requirements =>