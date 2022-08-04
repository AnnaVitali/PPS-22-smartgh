package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.model.plants.{PlantSelector, UploadPlants}
import it.unibo.pps.smartgh.view.SimulationView.{appSubtitle, appTitle}
import it.unibo.pps.smartgh.view.component
import javafx.scene.control.{CheckBox, Label}
import javafx.scene.layout.{BorderPane, VBox}
import javafx.stage.Stage
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{Assertions, BeforeAll, BeforeEach, Test, TestInstance}
import org.testfx.api.{FxRobot, FxToolkit}
import org.testfx.util.WaitForAsyncUtils
import org.testfx.assertions.api.Assertions as FXAssertions
import org.testfx.framework.junit5.{ApplicationExtension, ApplicationTest, Start}
import org.testfx.api.FxAssert.verifyThat
import org.hamcrest.MatcherAssert.assertThat
import org.testfx.matcher.base.NodeMatchers.{hasChildren, isVisible}
import org.testfx.matcher.control.LabeledMatchers
import org.testfx.matcher.control.LabeledMatchers.hasText
import scalafx.scene.Scene

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class SelectPlantViewTest extends AbstractViewTest:

  val selectYourPlantLabelId = "#selectYourPlantLabel"
  val plantSelectedLabelId = "#plantsSelectedLabel"
  val countLabelId = "#countLabel"
  val numberPlantsSelectedId = "#numberPlantsSelectedLabel"
  val selectablePlantsBoxId = "#selectablePlantsBox"
  val selectedPlantBoxId = "#selectedPlantsBox"
  private val path = System.getProperty("user.home") + "/pps/"
  private val file = "plants.txt"
  private val prologFile = "plants.pl"
  private val uploader = UploadPlants
  private var plantSelector: PlantSelector = _

  @BeforeAll
  def setPrologFile(): Unit =
    uploader.writePrologFile(path, file, prologFile)
    plantSelector = PlantSelector(path + prologFile)

  @Start
  private def start(stage: Stage): Unit =
    val baseView: BaseView = BaseView(appTitle, appSubtitle)
    startApplication(stage, baseView, SelectPlantView(null, baseView))

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

  @Test def testBeforePlantSelection(robot: FxRobot): Unit =
    val initialSelectedPlant = 0
    assert(
      robot
        .lookup(selectablePlantsBoxId)
        .queryAs(classOf[VBox])
        .getChildren
        .size == plantSelector.getAllAvailablePlants().length
    )
    assert(robot.lookup(selectedPlantBoxId).queryAs(classOf[VBox]).getChildren.size == initialSelectedPlant)

  @Test def testAfterPlantSelection(robot: FxRobot): Unit =
    val plantIndex = 0
    val selectedPlantNumber = 1
    val checkBox = robot.lookup(selectablePlantsBoxId).queryAs(classOf[VBox]).getChildren.get(plantIndex)
    //when:
    robot.clickOn(checkBox)

    //then:
    assert(robot.lookup(selectedPlantBoxId).queryAs(classOf[VBox]).getChildren.size == selectedPlantNumber)
    verifyThat(numberPlantsSelectedId, hasText(selectedPlantNumber.toString))

  @Test def testPlantDeselection(robot: FxRobot): Unit =
    val plantIndex = 0
    val selectedPlantNumber = 0
    val checkBox = robot.lookup(selectablePlantsBoxId).queryAs(classOf[VBox]).getChildren.get(plantIndex)
    //when:
    robot.doubleClickOn(checkBox)

    //then:
    assert(robot.lookup(selectedPlantBoxId).queryAs(classOf[VBox]).getChildren.size == selectedPlantNumber)
    verifyThat(numberPlantsSelectedId, hasText(selectedPlantNumber.toString))
