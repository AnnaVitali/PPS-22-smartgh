package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.PlantSelectorMVC
import it.unibo.pps.smartgh.view.component
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{Assertions, Test, TestInstance}
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.concurrent.Futures.timeout
import org.scalatest.time.{Milliseconds, Span}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.testfx.matcher.base.NodeMatchers.isVisible
import org.testfx.matcher.control.LabeledMatchers
import org.testfx.matcher.control.LabeledMatchers.hasText
import scalafx.scene.Scene
/** This class contains the tests to verify that the [[SelectPlantViewModule]] work correctly. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class SelectPlantViewModuleTest extends AbstractViewTest:

  private val selectYourPlantLabelId = "#selectYourPlantLabel"
  private val plantSelectedLabelId = "#plantsSelectedLabel"
  private val countLabelId = "#countLabel"
  private val numberPlantsSelectedId = "#numberPlantsSelectedLabel"
  private val selectablePlantsBoxId = "#selectablePlantsBox"
  private val selectedPlantBoxId = "#selectedPlantsBox"
  private val errorLabelId = "#errorLabel"
  private val changeSceneButtonId = "#changeSceneButton"
  private var mvc: PlantSelectorMVC = _

  @Start
  private def start(stage: Stage): Unit =
    val simulationMVC = SimulationMVC(stage)
    mvc = PlantSelectorMVC(simulationMVC)
    simulationMVC.simulationView.start(mvc.selectPlantView)

  @Test def testLabelsSelectPlantAndPlantSelected(robot: FxRobot): Unit =
    val selectYourPlantsText = "Select your plants:"
    val plantsSelectedText = "Plants selected:"
    verifyThat(selectYourPlantLabelId, isVisible)
    verifyThat(selectYourPlantLabelId, hasText(selectYourPlantsText))
    verifyThat(plantSelectedLabelId, isVisible)
    verifyThat(plantSelectedLabelId, hasText(plantsSelectedText))

  @Test def testLabelsCount(robot: FxRobot): Unit =
    val countText = "Count: "
    val numberPlantText = "0"
    verifyThat(countLabelId, isVisible)
    verifyThat(countLabelId, hasText(countText))
    verifyThat(numberPlantsSelectedId, isVisible)
    verifyThat(numberPlantsSelectedId, hasText(numberPlantText))

  @Test def testLabelError(robot: FxRobot): Unit =
    val errorText = "At least one plant must be selected"
    val startSimulationButton = robot.lookup(changeSceneButtonId).queryAs(classOf[Button])
    verifyThat(errorLabelId, isVisible)
    verifyThat(errorLabelId, hasText(""))
    //when
    robot.clickOn(startSimulationButton)

    //then
    verifyThat(errorLabelId, hasText(errorText))

  @Test def testBeforePlantSelection(robot: FxRobot): Unit =
    val initialSelectedPlant = 0
    assert(
      robot
        .lookup(selectablePlantsBoxId)
        .queryAs(classOf[VBox])
        .getChildren
        .size == mvc.plantSelectorModel.getAllAvailablePlants.length
    )
    assert(robot.lookup(selectedPlantBoxId).queryAs(classOf[VBox]).getChildren.size == initialSelectedPlant)

  @Test def testAfterPlantSelection(robot: FxRobot): Unit =
    val plantIndex = 0
    val selectedPlantNumber = 1
    val checkBox = robot.lookup(selectablePlantsBoxId).queryAs(classOf[VBox]).getChildren.get(plantIndex)
    //when:
    robot.clickOn(checkBox)

    //then:
    eventually(timeout(Span(8000, Milliseconds))) {
      assertEquals(robot.lookup(selectedPlantBoxId).queryAs(classOf[VBox]).getChildren.size, selectedPlantNumber)
      verifyThat(numberPlantsSelectedId, hasText(selectedPlantNumber.toString))
    }

  //noinspection DfaConstantConditions
  @Test def testPlantDeselection(robot: FxRobot): Unit =
    val plantIndex = 0
    val selectedPlantNumber = 0
    val checkBox = robot.lookup(selectablePlantsBoxId).queryAs(classOf[VBox]).getChildren.get(plantIndex)
    //when:
    robot.doubleClickOn(checkBox)

    //then:
    assertEquals(robot.lookup(selectedPlantBoxId).queryAs(classOf[VBox]).getChildren.size, selectedPlantNumber)
    verifyThat(numberPlantsSelectedId, hasText(selectedPlantNumber.toString))
