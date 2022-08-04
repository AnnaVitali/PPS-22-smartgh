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
  trait GHDivisionView extends ViewComponent[VBox]:
    /** Draws the greenhouse division according to the rows and cols.
     * @param rows of the greenhouse grid
     * @param cols of the greenhouse grid
     * @param areas list of areas componing the greenhouse
     * */
    def paintDivision(rows: Int, cols: Int, areas: List[AreaViewModule.AreaView]): Unit

  /** A trait for defining the view instance.*/
  trait Provider:
    val ghDivisionView: GHDivisionView

  type Requirements = GHControllerModule.Provider
  /** A trait that represents the greenhouse division view component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the greenhouse division view.*/
    class GreenHouseDivisionViewImpl()
      extends AbstractViewComponent[VBox]("ghDivision.fxml")
        with GHDivisionView:
      private val env = GridPane()
      override val component: VBox = loader.load[VBox]

      @FXML
      var ghDivision: VBox = _

      ghDivision.getChildren.add(env)
      env.setHgap(5)
      env.setVgap(5)

      override def paintDivision(rows: Int, cols: Int, areas: List[AreaViewModule.AreaView]): Unit =
        Platform.runLater(() =>
          env.getChildren.clear()
          var count = 0
          for
            c <- 0 until cols
            r <- 0 until rows
          do
            val i = count
            env.add(areas(i), c, r)
            count = count + 1
        )

  /** Trait that combine provider and component for greenhouse division view.*/
  trait Interface extends Provider with Component:
    self: Requirements =>