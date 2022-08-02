package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.model.greenhouse.GreenHouse
import it.unibo.pps.smartgh.view.component.*
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.{BeforeAll, TestInstance}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, ApplicationTest, Start}
import org.testfx.matcher.base.NodeMatchers.{hasChildren, isVisible}
import org.testfx.matcher.control.LabeledMatchers.hasText
import org.testfx.util.WaitForAsyncUtils
import scalafx.scene.Scene

/** This class contains the tests realized to verify the correct behavior of [[GreenHouseDivisionView]]. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class GreenHouseDivisionViewTest:

  val globalGH = "#ghDivision"
  val model = GreenHouse(
    List("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9", "p10", "p11", "p12"),
    List("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9", "p10", "p11", "p12"),
    "Rome"
  )

  @BeforeAll
  def setup(): Unit =
    System.setProperty("testfx.robot", "glass")
    System.setProperty("testfx.headless", "true")
    System.setProperty("java.awt.headless", "true")
    System.setProperty("prism.order", "sw")
    System.setProperty("prism.text", "t2k")
    WaitForAsyncUtils.checkAllExceptions = false
    WaitForAsyncUtils.autoCheckException = false

  @Start
  private def start(stage: Stage): Unit =
    val scene: Scene = Scene(stage.getWidth, stage.getHeight)
    val baseView: ViewComponent[VBox] = BaseView("title", "subtitle")

    stage.setResizable(true)
    baseView.getChildren.add(GreenHouseDivisionView()) //init view
    scene.root.value = baseView
    stage.setScene(scene)
    stage.show()

  @Test def testLabels(robot: FxRobot): Unit =

    verifyThat(globalGH, isVisible())
    verifyThat(globalGH, hasChildren(model.plants.length, ""))
