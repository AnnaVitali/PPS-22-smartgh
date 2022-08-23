package it.unibo.pps.smartgh.view.component.areaParameters

import javafx.scene.control.ToggleButton
import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.{assertFalse, assertTrue}
import org.junit.jupiter.api.{Test, TestInstance}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.testfx.matcher.base.NodeMatchers.isVisible
import org.testfx.matcher.control.LabeledMatchers.hasText
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule.{VentilationText, AtomiserText}

/** This class contains the tests to verify that the [[AreaAirHumidityViewModule]] work correctly. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class AreaAirHumidityViewModuleTest extends AbstractAreaParametersViewTest("Air humidity", "Humidity"):

  private val ventilationBtnId = "#ventilationBtn"
  private val atomiserBtnId = "#atomiserBtn"

  @Start
  override def start(stage: Stage): Unit = super.start(stage)

  @Test
  def testVentilationBtn(robot: FxRobot): Unit =
    val button = getToggleButton(robot, ventilationBtnId)
    basicToggleButtonTest(button, ventilationBtnId, VentilationText.ACTIVATE.text, false)

    robot.clickOn(ventilationBtnId)

    assertTrue(button.isSelected)
    verifyThat(ventilationBtnId, hasText(VentilationText.DEACTIVATE.text))

  @Test
  def testAtomiserBtn(robot: FxRobot): Unit =
    val button = getToggleButton(robot, atomiserBtnId)
    basicToggleButtonTest(button, atomiserBtnId, AtomiserText.ACTIVATE.text, false)

    robot.clickOn(atomiserBtnId)

    assertTrue(button.isSelected)
    assertTrue(button.isSelected)
    verifyThat(atomiserBtnId, hasText(AtomiserText.DEACTIVATE.text))

  @Test
  def testNotSelectableBothActions(robot: FxRobot): Unit =
    val ventilationBtn = getToggleButton(robot, ventilationBtnId)
    val atomiserBtn = getToggleButton(robot, atomiserBtnId)

    robot.clickOn(atomiserBtnId)
    robot.clickOn(ventilationBtnId)

    assertTrue(ventilationBtn.isSelected)
    verifyThat(ventilationBtnId, hasText(VentilationText.DEACTIVATE.text))
    assertFalse(atomiserBtn.isSelected)
    verifyThat(atomiserBtnId, hasText(AtomiserText.ACTIVATE.text))
