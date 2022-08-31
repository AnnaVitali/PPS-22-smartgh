package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.model.greenhouse.Environment
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.mvc.component.EnvironmentMVC
import it.unibo.pps.smartgh.mvc.{SimulationMVC, component}
import javafx.scene.control.Slider
import javafx.stage.Stage
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{Test, TestInstance}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.testfx.matcher.base.NodeMatchers.isVisible
import org.testfx.matcher.control.LabeledMatchers.hasText

import java.util.Optional

/** This class contains the tests to verify that the [[EnvironmentViewModule]] work correctly. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class EnvironmentViewModuleTest extends AbstractViewTest:

  @Start
  private def start(stage: Stage): Unit =
    val simulationMVC = SimulationMVC(stage)
    simulationMVC.simulationController.environment = Environment("Rome", "41.8931", "12.4828")
    simulationMVC.simulationController.plantsSelected =
      List(Plant("carrot", "carrot aurantina f1"), Plant("eggplant", "solanum melongena"))
    simulationMVC.simulationView.start(EnvironmentMVC(simulationMVC).environmentView)

  @Test def testEnvironmentLabels(robot: FxRobot): Unit =
    verifyThat("#locationLabel", isVisible)
    verifyThat("#setLocationLabel", hasText("default"))
    verifyThat("#dayLabel", isVisible)
    verifyThat("#setDayLabel", hasText("default"))
    verifyThat("#temperatureLabel", isVisible)
    verifyThat("#setTemperatureLabel", hasText("default"))
    verifyThat("#humidityLabel", isVisible)
    verifyThat("#setHumidityLabel", hasText("default"))
    verifyThat("#uvIndexLabel", isVisible)
    verifyThat("#setUvIndexLabel", hasText("default"))
    verifyThat("#luxLabel", isVisible)
    verifyThat("#setLuxLabel", hasText("default"))
    verifyThat("#conditionLabel", isVisible)
    verifyThat("#setConditionLabel", hasText("default"))
    verifyThat("#timeLabel", isVisible)
    verifyThat("#timeElapsedLabel", hasText("00:00:00"))
    verifyThat("#timeSpeedLabel", isVisible)

  @Test def testSpeedSlider(robot: FxRobot): Unit =
    verifyThat("#timeSpeedSlider", isVisible)
    assert(
      robot
        .lookup("#timeSpeedSlider")
        .queryAs(classOf[Slider])
        .getValue == 1
    )
    assert(
      robot
        .lookup("#timeSpeedSlider")
        .queryAs(classOf[Slider])
        .getMax == 10
    )
