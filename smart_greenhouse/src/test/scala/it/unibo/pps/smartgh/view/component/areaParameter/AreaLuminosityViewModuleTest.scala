package it.unibo.pps.smartgh.view.component.areaParameter

import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import javafx.scene.control.Slider
import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.{Test, TestInstance}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.testfx.matcher.control.ButtonMatchers

/** This class contains the tests to verify that the [[AreaLuminosityViewModule]] work correctly. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class AreaLuminosityViewModuleTest extends AbstractAreaParameterViewTest("Luminosity", "Brightness"):

  private val upShieldingBtnId = "#upShieldingBtn"
  private val downShieldingBtnId = "#downShieldingBtn"
  private val lampBrightnessSliderId = "#lampBrightnessSlider"
  private val lampFactor = 1000.0
  private var areaModel: AreaModel = _

  @Start
  override def start(stage: Stage): Unit =
    super.start(stage)
    areaModel = areaDetailsMVC.areaModel

  @Test
  def testButtons(robot: FxRobot): Unit =
    basicToggleButtonTest(getToggleButton(robot, upShieldingBtnId), upShieldingBtnId, "Up", true)
    basicToggleButtonTest(getToggleButton(robot, downShieldingBtnId), downShieldingBtnId, "Down", false)

  @Test
  def testSlider(robot: FxRobot): Unit =
    val slider = robot.lookup(lampBrightnessSliderId).queryAs(classOf[Slider])
    assertEquals(areaModel.getAreaComponent.brightnessOfTheLamps, slider.getValue * lampFactor)
