package it.unibo.pps.smartgh.view.component

import cats.syntax.eq.catsSyntaxEq
import it.unibo.pps.smartgh.controller.GreenHouseDivisionController
import it.unibo.pps.smartgh.view.SimulationView
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

/** A trait that represents the green house division view of the application. */
trait GreenHouseDivisionView extends ViewComponent[VBox]:
  def paintDivision(rows: Int, cols: Int, areas: List[(String, Boolean)]): Unit

/** Object that can be used to create new instances of [[GreenHouseDivisionView]]. */
object GreenHouseDivisionView:

  /** Creates a new [[GreenHouseDivisionView]] component.
    * @return
    *   a new instance of [[GreenHouseDivisionView]]
    */
  def apply(): GreenHouseDivisionView = GreenHouseDivisionViewImpl()

  /** Implementation of [[GreenHouseDivisionView]]. */
  private class GreenHouseDivisionViewImpl()
      extends AbstractViewComponent[VBox]("ghDivision.fxml")
      with GreenHouseDivisionView:
    private val env = GridPane()
    private val controller: GreenHouseDivisionController = GreenHouseDivisionController()
    override val component: VBox = loader.load[VBox]

    @FXML
    var ghDivision: VBox = _

    controller.view = this
    controller.updateView()


    override def paintDivision(rows: Int, cols: Int, areas: List[(String, Boolean)]): Unit =

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
          controller.openArea(areas(i))
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
