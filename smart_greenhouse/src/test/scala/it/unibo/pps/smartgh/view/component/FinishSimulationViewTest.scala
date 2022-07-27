package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest
import org.testfx.api.FxAssert.verifyThat
import org.testfx.matcher.base.NodeMatchers.isVisible
import org.testfx.matcher.control.LabeledMatchers.hasText
import scalafx.scene.Scene

class FinishSimulationViewTest extends ApplicationTest:

  val finishSimulationLabelId = "#simulationEndedLabel"
  val startNewSimulationButtonlId = "#startNewSimulationButton"

  override def start(stage: Stage): Unit =
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
