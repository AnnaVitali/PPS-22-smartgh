package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component
import javafx.scene.layout.{BorderPane, VBox}
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.{BeforeAll, TestInstance}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.{ApplicationExtension, ApplicationTest, Start}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.matcher.base.NodeMatchers.isVisible
import org.testfx.matcher.control.LabeledMatchers.hasText
import org.testfx.util.WaitForAsyncUtils
import scalafx.scene.Scene

/** This class contains the tests realized to verify the correct behavior of [[FinishSimulationView]]. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class FinishSimulationViewTest extends AbstractViewTest:

  val finishSimulationLabelId = "#simulationEndedLabel"
  val startNewSimulationButtonlId = "#changeSceneButton"

  @Start
  private def start(stage: Stage): Unit =
    val baseView: BaseView = BaseView(appTitle, appSubtitle)
    startApplication(stage, baseView, FinishSimulationView(null, baseView))

  @Test def testLabel(robot: FxRobot): Unit =
    val simulationEndedText = "Simulation ended!"
    verifyThat(finishSimulationLabelId, isVisible)
    verifyThat(finishSimulationLabelId, hasText(simulationEndedText))

  @Test def testButton(robot: FxRobot): Unit =
    val startNewSimulationText = "Start a new simulation"
    verifyThat(startNewSimulationButtonlId, isVisible)
    verifyThat(startNewSimulationButtonlId, hasText(startNewSimulationText))
//TODO verify button click with robot
