package it.unibo.pps.smartgh.view.component.areaParameter

import it.unibo.pps.smartgh.view.component.areaParameter.AreaAirHumidityViewModule.{AtomiserText, VentilationText}
import javafx.scene.control.ToggleButton
import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.{assertFalse, assertTrue}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{Test, TestInstance}
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.concurrent.Futures.timeout
import org.scalatest.time.{Milliseconds, Span}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.testfx.matcher.base.NodeMatchers.{isEnabled, isVisible}
import org.testfx.matcher.control.LabeledMatchers.hasText

/** This class contains the tests to verify that the [[AreaAirHumidityViewModule]] work correctly. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class AreaAirHumidityViewModuleTest extends AbstractAreaParameterViewTest("Air humidity", "Humidity"):

  private val ventilationBtnId = "#ventilationBtn"
  private val atomiserBtnId = "#atomiserBtn"

  @Start
  override def start(stage: Stage): Unit = super.start(stage)

  @Test
  def testVentilationBtn(robot: FxRobot): Unit =
    val button = getToggleButton(robot, ventilationBtnId)
    basicToggleButtonTest(button, ventilationBtnId, VentilationText.ACTIVATE.text, false)

    robot.clickOn(ventilationBtnId)

    eventually(timeout(Span(20000, Milliseconds))) {
      assertTrue(button.isSelected)
      verifyThat(ventilationBtnId, hasText(VentilationText.DEACTIVATE.text))
    }

  @Test
  def testAtomiserBtn(robot: FxRobot): Unit =
    val button = getToggleButton(robot, atomiserBtnId)
    basicToggleButtonTest(button, atomiserBtnId, AtomiserText.ACTIVATE.text, false)
    eventually(timeout(Span(20000, Milliseconds))) {
      verifyThat(atomiserBtnId, isEnabled)
    }

    robot.clickOn(atomiserBtnId)

    eventually(timeout(Span(20000, Milliseconds))) {
      assertTrue(button.isSelected)
      verifyThat(atomiserBtnId, hasText(AtomiserText.DEACTIVATE.text))
    }
