package it.unibo.pps.smartgh.greenhouse.view.component

import it.unibo.pps.smartgh.greenhouse.GreenHouse
import it.unibo.pps.smartgh.view.component.*
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest
import org.testfx.api.FxAssert.verifyThat
import org.testfx.matcher.base.NodeMatchers.{hasChildren, isVisible}
import org.testfx.matcher.control.LabeledMatchers.hasText
import scalafx.scene.Scene
/** This class contains the tests realized to verify the correct behavior of [[GreenHouseDivisionView]]. */
class GreenHouseDivisionViewTest extends ApplicationTest:

  val globalGH = "#ghDivision"
  val model = GreenHouse(List("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9", "p10", "p11", "p12"),
    List("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9", "p10", "p11", "p12"),
    "Rome")

  override def start(stage: Stage): Unit =
    val scene: Scene = Scene(stage.getMaxWidth, stage.getMaxHeight)
    val baseView: ViewComponent[VBox] = BaseView("title", "subtitle")

    stage.setResizable(true)
    baseView.getChildren.add(GreenHouseDivisionView()) //init view
    scene.root.value = baseView
    stage.setScene(scene)
    stage.show()

  @Test def testLabels(): Unit =

    verifyThat(globalGH, isVisible())
    verifyThat(globalGH, hasChildren(model.plants.length,""))