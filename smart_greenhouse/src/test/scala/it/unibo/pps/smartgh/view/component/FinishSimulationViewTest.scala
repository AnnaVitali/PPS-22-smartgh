package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.junit.Test
import org.junit.jupiter.api.{BeforeAll, TestInstance}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.{ApplicationExtension, ApplicationTest, Start}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.matcher.base.NodeMatchers.isVisible
import org.testfx.matcher.control.LabeledMatchers.hasText
import org.testfx.util.WaitForAsyncUtils
import scalafx.scene.Scene

/** This class contains the tests realized to verify the correct behavior of [[FinishSimulationView]]. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class FinishSimulationViewTest:

  val finishSimulationLabelId = "#simulationEndedLabel"
  val startNewSimulationButtonlId = "#startNewSimulationButton"

  @BeforeAll
  def setup(): Unit =
    System.setProperty("testfx.robot", "glass")
    System.setProperty("testfx.headless", "true")
    System.setProperty("java.awt.headless", "true")
    System.setProperty("prism.order", "sw")
    System.setProperty("prism.text", "t2k")
    System.setProperty("headless.geometry", "1600x1200-32")
    WaitForAsyncUtils.checkAllExceptions = false
    WaitForAsyncUtils.autoCheckException = false

  @Start
  private def start(stage: Stage): Unit =
    val scene: Scene = Scene(stage.getMaxWidth, stage.getMaxHeight)
    val baseView: ViewComponent[VBox] = BaseView("Smart Greenhouse", "Simulate your smart greenhouse")

    stage.setResizable(true)
    baseView.getChildren.add(FinishSimulationView())
    scene.root.value = baseView
    stage.setScene(scene)
    stage.show()

  @Test def testLabel(): Unit =
    val simulationEndedText = "Simulation ended!"
    verifyThat(finishSimulationLabelId, isVisible)
    verifyThat(finishSimulationLabelId, hasText(simulationEndedText))

  @Test def testButton(): Unit =
    val startNewSimulationText = "Start a new simulation"
    verifyThat(startNewSimulationButtonlId, isVisible)
    verifyThat(startNewSimulationButtonlId, hasText(startNewSimulationText))
//TODO verify button click with robot
