package it.unibo.pps.smartgh.view.component

import cats.syntax.eq.catsSyntaxEq
import it.unibo.pps.smartgh.controller.component.GHControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.ScrollPane
import javafx.scene.layout.GridPane
import scalafx.scene.layout.Pane

import scala.language.postfixOps
/** Implementation of the [[GHViewModule]]. */
object GHViewModule:
  /** A trait that represents the green house division view of the application. */
  trait GreenHouseView extends ViewComponent[ScrollPane]:
    /** Draws the greenhouse division according to the rows and cols.
      * @param areas
      *   list of areas composing the greenhouse
      */
    def paintDivision(areas: List[AreaViewModule.AreaView]): Unit

  /** A trait for defining the view instance. */
  trait Provider:
    /** Greenhouse division view. */
    val ghDivisionView: GreenHouseView

  /** Requirements for the [[GreenHouseView]]. */
  type Requirements = GHControllerModule.Provider

  /** A trait that represents the greenhouse division view component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the greenhouse division view.
      * @return
      *   the implementation of the [[GreenHouseDivisionViewImpl]].
      */
    class GreenHouseDivisionViewImpl() extends AbstractViewComponent[ScrollPane]("ghDivision.fxml") with GreenHouseView:

      //noinspection VarCouldBeVal
      @FXML
      protected var env: GridPane = _

      @FXML
      protected var scroll: ScrollPane = _

      env.setHgap(5)
      env.setVgap(5)

      override def paintDivision(areas: List[AreaViewModule.AreaView]): Unit =
        val cols = 5
        Platform.runLater(() =>
          env.getChildren.clear()
          //noinspection VarCouldBeVal
          var count = 0
          for
            r <- 0 until Math.ceil(areas.length / cols).toInt + (if areas.length % cols == 0 then 0 else 1)
            c <- 0 until cols
            if count < areas.length
          do
            env.add(areas(count), c, r)
            count = count + 1
        )

  /** Trait that combine provider and component for greenhouse division view. */
  trait Interface extends Provider with Component:
    self: Requirements =>
