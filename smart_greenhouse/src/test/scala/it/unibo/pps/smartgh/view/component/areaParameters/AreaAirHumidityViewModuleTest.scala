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

/** This class contains the tests to verify that the [[AreaAirHumidityViewModule]] work correctly. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class AreaAirHumidityViewModuleTest extends AbstractAreaParametersViewTest("Air humidity", "Humidity"):

  private val ventilationBtnId = "#ventilationBtn"
  private val atomiserBtnId = "#atomiserBtn"

  private def baseTestButton(robot: FxRobot, buttonId: String): Unit =
    val button = robot.lookup(buttonId).queryAs(classOf[ToggleButton])
    verifyThat(buttonId, isVisible)
    assertFalse(button.isSelected)

    robot.clickOn(buttonId)
    assertTrue(button.isSelected)

  @Start
  override def start(stage: Stage): Unit = super.start(stage)

  @Test
  def testVentilationBtn(robot: FxRobot): Unit = baseTestButton(robot, ventilationBtnId)

  @Test
  def testAtomiserBtn(robot: FxRobot): Unit = baseTestButton(robot, atomiserBtnId)
