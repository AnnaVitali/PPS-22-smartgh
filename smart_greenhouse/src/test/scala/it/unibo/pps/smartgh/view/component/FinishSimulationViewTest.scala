package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.SelectCityMVC
import it.unibo.pps.smartgh.view.component
import javafx.scene.layout.{BorderPane, VBox}
import javafx.stage.Stage
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{BeforeAll, Test, TestInstance}
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.concurrent.Futures.timeout
import org.scalatest.time.{Milliseconds, Span}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, ApplicationTest, Start}
import org.testfx.matcher.base.NodeMatchers.isVisible
import org.testfx.matcher.control.LabeledMatchers.hasText
import org.testfx.util.WaitForAsyncUtils
import scalafx.scene.Scene

/** This class contains the tests realized to verify the correct behavior of [[FinishSimulationView]]. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class FinishSimulationViewTest extends AbstractViewTest:

  val finishSimulationLabelId = "#simulationEndedLabel"
  val startNewSimulationButtonId = "#changeSceneButton"

  @Start
  private def start(stage: Stage): Unit =
    val simulationMVC = SimulationMVC(stage)
    simulationMVC.simulationView.start(FinishSimulationView(simulationMVC))

  @Test def testLabel(robot: FxRobot): Unit =
    val simulationEndedText = "Simulation ended!"
    verifyThat(finishSimulationLabelId, isVisible)
    verifyThat(finishSimulationLabelId, hasText(simulationEndedText))

  @Test def testButton(robot: FxRobot): Unit =
    val startNewSimulationText = "Start a new simulation"
    eventually(timeout(Span(1000, Milliseconds))) {
      verifyThat(startNewSimulationButtonId, isVisible)
      verifyThat(startNewSimulationButtonId, hasText(startNewSimulationText))
    }
