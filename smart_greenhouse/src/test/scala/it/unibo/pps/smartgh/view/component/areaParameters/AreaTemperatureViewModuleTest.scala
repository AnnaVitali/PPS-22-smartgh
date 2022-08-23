package it.unibo.pps.smartgh.view.component.areaParameters

import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.{assertFalse, assertTrue}
import org.junit.jupiter.api.{Test, TestInstance}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.concurrent.Futures.timeout
import org.scalatest.time.{Milliseconds, Span}
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, Start}

/** This class contains the tests to verify that the [[AreaSoilMoistureViewModule]] work correctly. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class AreaTemperatureViewModuleTest extends AbstractAreaParametersViewTest("Temperature", "Temperature"):

  private val openStructureBtnId = "#openStructureBtn"
  private val closeStructureBtnId = "#closeStructureBtn"
  private val minusTempBtnId = "#minusTempBtn"
  private val plusTempBtnId = "#plusTempBtn"
  private val regulateTempLabelId = "#regulateTempLabel"

  @Start
  override def start(stage: Stage): Unit = super.start(stage)

  @Test
  def testButtons(robot: FxRobot): Unit =
    basicToggleButtonTest(getToggleButton(robot, openStructureBtnId), openStructureBtnId, "Open", true)
    basicToggleButtonTest(getToggleButton(robot, closeStructureBtnId), closeStructureBtnId, "Close", false)

  @Test
  def closeStructure(robot: FxRobot): Unit =
    robot.clickOn(closeStructureBtnId)
    basicToggleButtonTest(getToggleButton(robot, closeStructureBtnId), closeStructureBtnId, "Close", true)

    assertFalse(robot.lookup(minusTempBtnId).queryButton().isDisabled)
    assertFalse(robot.lookup(plusTempBtnId).queryButton().isDisabled)

  @Test
  def plusTemperature(robot: FxRobot): Unit = testRegulateTemperature(robot, plusTempBtnId, _ > _)

  @Test
  def minusTemperature(robot: FxRobot): Unit = testRegulateTemperature(robot, minusTempBtnId, _ < _)

  @Test
  def testPlusTempAction(robot: FxRobot): Unit = testActions(robot, plusTempBtnId, _ != _)

  @Test
  def testMinusTempAction(robot: FxRobot): Unit = testActions(robot, minusTempBtnId, _ != _)

  private def testRegulateTemperature(robot: FxRobot, buttonId: String, condition: (Double, Double) => Boolean): Unit =
    closeStructure(robot)
    val regulatedTemp = robot.lookup(regulateTempLabelId).queryLabeled()
    val defaultTempValue = regulatedTemp.getText.toDouble
    robot.clickOn(buttonId)
    eventually(timeout(Span(1000, Milliseconds))) {
      assertTrue(condition(regulatedTemp.getText.toDouble, defaultTempValue))
    }
