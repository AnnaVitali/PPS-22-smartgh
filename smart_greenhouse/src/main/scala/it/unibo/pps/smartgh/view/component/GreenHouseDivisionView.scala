package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
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
trait GreenHouseDivisionView extends ViewComponent[VBox]

/** Object that can be used to create new instances of [[GreenHouseDivisionView]]. */
object GreenHouseDivisionView:

  /** Creates a new [[GreenHouseDivisionView]] component.
   * @return
   *   a new instance of [[GreenHouseDivisionView]]
   */
  def apply(rows: Int, cols: Int): GreenHouseDivisionViewImpl = GreenHouseDivisionViewImpl().build(rows, cols)

  /** Implementation of [[GreenHouseDivisionView]]. */
  class GreenHouseDivisionViewImpl() extends AbstractViewComponent[VBox]("ghDivision.fxml") with GreenHouseDivisionView:
    self =>
    private val env = GridPane()
    override val component: VBox = loader.load[VBox]

    @FXML
    var ghDivision: VBox = _

    def build(rows: Int, cols: Int): GreenHouseDivisionViewImpl =
      ghDivision.getChildren.add(0, env)
      for
        c <- 0 until cols
        r <- 0 until rows
      do
        val rect = new VBox()

        val params = new VBox()
        params.setAlignment(Pos.Center)
        params.getChildren.add(0, new Label("param 1"))
        params.getChildren.add(0, new Label("param 2"))
        params.getChildren.add(0, new Label("param 3"))

        val area = new Button
        area.setGraphic(params)
        area.prefHeight = 100
        area.prefWidth = 100
        area.style = "-fx-background-color: #33cc33"
        area.onMouseEntered = _ => area.graphic.get().asInstanceOf[VBox].getChildren.forEach(c =>
          c.asInstanceOf[Label].setStyle("-fx-text-fill: #ffffff ; -fx-background-color: #33cc33")
        )
        area.onMouseExited = _ => area.graphic.get().asInstanceOf[VBox].getChildren.forEach(c =>
          c.asInstanceOf[Label].setStyle("-fx-text-fill: #000000 ; -fx-background-color: #33cc33")
        )
        area.onMouseClicked = _ => println("Go TO area X")

        val label = Label()
        label.setText("plant 1")

        rect.getChildren.add(0, area)
        rect.getChildren.add(1, label)
        rect.setSpacing(5)
        rect.setAlignment(Pos.Center)
        rect.setStyle("-fx-border-color: #000000")
        env.add(rect, c, r)

      env.setHgap(5)
      env.setVgap(5)
      this

