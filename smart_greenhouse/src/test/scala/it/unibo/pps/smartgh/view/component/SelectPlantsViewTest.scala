package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.city.CitiesSearcher
import it.unibo.pps.smartgh.view.SimulationView.{appSubtitle, appTitle}
import it.unibo.pps.smartgh.view.component
import javafx.scene.control.Label
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{Assertions, BeforeAll, BeforeEach, Test, TestInstance}
import org.testfx.api.{FxRobot, FxToolkit}
import org.testfx.util.WaitForAsyncUtils
import org.testfx.assertions.api.Assertions as FXAssertions
import org.testfx.framework.junit5.{ApplicationExtension, ApplicationTest, Start}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.matcher.base.NodeMatchers.isVisible
import org.testfx.matcher.control.LabeledMatchers
import org.testfx.matcher.control.LabeledMatchers.hasText
import scalafx.scene.Scene

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class SelectPlantsViewTest:

  val selectYourPlantLabelId = "#selectYourPlantLabel"
  val plantSelectedLabelId = "#plantsSelectedLabel"
  val countLabelId = "#countLabel"

  @BeforeAll
  def setup(): Unit =
    System.setProperty("testfx.robot", "glass")
    System.setProperty("testfx.headless", "true")
    System.setProperty("java.awt.headless", "true")
    System.setProperty("prism.order", "sw")
    System.setProperty("prism.text", "t2k")
    WaitForAsyncUtils.checkAllExceptions = false;
    WaitForAsyncUtils.autoCheckException = false;

  @Start
  private def start(stage: Stage): Unit =
    val scene: Scene = Scene(stage.getWidth, stage.getHeight)
    val baseView: ViewComponent[VBox] = BaseView("title", "subtitle")

    stage.setResizable(true)
    baseView.getChildren.add(SelectPlantsView()) //init view
    scene.root.value = baseView
    stage.setScene(scene)
    stage.show()

  @Test def testLabels(robot: FxRobot): Unit =
    val selectYourPlantsText = "Select your plants:"
    val plantsSelectedText = "Plants selected:"
    val countText = "Count:"

    verifyThat(selectYourPlantLabelId, isVisible)
    verifyThat(selectYourPlantLabelId, hasText(selectYourPlantsText))
    verifyThat(plantSelectedLabelId, isVisible)
    verifyThat(plantSelectedLabelId, hasText(plantsSelectedText))
    verifyThat(countLabelId, isVisible)
    verifyThat(countLabelId, hasText(countText))
