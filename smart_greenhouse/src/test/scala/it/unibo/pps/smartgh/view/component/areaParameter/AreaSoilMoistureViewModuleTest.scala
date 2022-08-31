package it.unibo.pps.smartgh.view.component.areaParameter

import javafx.stage.Stage
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
class AreaSoilMoistureViewModuleTest extends AbstractAreaParameterViewTest("Soil moisture", "Soil moisture"):

  private val movingSoilBtnId = "#movingSoilBtn"
  private val wateringBtnId = "#wateringBtn"

  @Start
  override def start(stage: Stage): Unit = super.start(stage)

  @Test
  def testButtons(robot: FxRobot): Unit =
    basicButtonTest(robot.lookup(movingSoilBtnId).queryButton(), movingSoilBtnId, "Moving the soil")
    basicButtonTest(robot.lookup(wateringBtnId).queryButton(), wateringBtnId, "Watering")

  @Test
  def testMovingTheSoil(robot: FxRobot): Unit = testActions(robot, movingSoilBtnId, _ < _)

  @Test
  def testWatering(robot: FxRobot): Unit = testActions(robot, wateringBtnId, _ > _)
