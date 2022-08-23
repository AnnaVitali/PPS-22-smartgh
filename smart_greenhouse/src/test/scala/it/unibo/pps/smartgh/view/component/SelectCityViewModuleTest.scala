package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.model.greenhouse.Environment
import it.unibo.pps.smartgh.mvc.component.SelectCityMVC
import it.unibo.pps.smartgh.mvc.{SimulationMVC, component}
import it.unibo.pps.smartgh.view.SimulationViewModule
import it.unibo.pps.smartgh.view.component.SelectCityViewModule
import it.unibo.pps.smartgh.view.component.SelectCityViewModule.SelectCityView
import javafx.scene.control.TextField
import javafx.scene.layout.{BorderPane, VBox}
import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{BeforeAll, Test, TestInstance}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.testfx.matcher.base.NodeMatchers.{isEnabled, isFocused, isVisible}
import org.testfx.matcher.control.{LabeledMatchers, TextInputControlMatchers}
import org.testfx.util.WaitForAsyncUtils
import scalafx.scene.Scene

/** This class contains the tests to verify that the [[SelectCityViewModule]] work correctly. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class SelectCityViewModuleTest extends AbstractViewTest:

  private var selectCityView: SelectCityView = _
  private val textFieldId = "#selectCityTextField"
  private val nextButtonId = "#changeSceneButton"
  private val errorLabel = "#errorLabel"

  private def writeCityAndVerifyField(robot: FxRobot, city: String): Unit =
    verifyThat(textFieldId, TextInputControlMatchers.hasText(""))
    robot.clickOn(textFieldId)
    robot.write(city)
    verifyThat(textFieldId, TextInputControlMatchers.hasText(city))

  @Start
  private def start(stage: Stage): Unit =
    val baseView: BaseView = BaseView(appTitle, appSubtitle)
    simulationMVC = SimulationMVC(stage)
    selectCityView = component.SelectCityMVC(simulationMVC, baseView).selectCityView
    startApplication(stage, baseView, selectCityView)

  @Test
  def testTextField(robot: FxRobot): Unit =
    verifyThat(textFieldId, isVisible)
    verifyThat(textFieldId, isEnabled)

  @Test
  def testAutoCompletionPopup(robot: FxRobot): Unit =
    val char = "A"
    writeCityAndVerifyField(robot, char)
    verifyThat(textFieldId, TextInputControlMatchers.hasText(char))
    selectCityView.autoCompletionBinding.getAutoCompletionPopup.getSuggestions.forEach(city =>
      assertTrue(city.startsWith(char))
    )

  @Test
  def testEmptyCityError(robot: FxRobot): Unit =
    robot.clickOn(nextButtonId)
    verifyThat(errorLabel, isVisible)
    verifyThat(errorLabel, LabeledMatchers.hasText("Please select a city"))

  @Test
  def testWrongCityError(robot: FxRobot): Unit =
    val wrongCity = "Wrong city"
    writeCityAndVerifyField(robot, wrongCity)
    robot.clickOn(nextButtonId)
    verifyThat(errorLabel, isVisible)
    verifyThat(errorLabel, LabeledMatchers.hasText("The selected city is not valid"))

  @Test
  def testSelectCityAndSave(robot: FxRobot): Unit =
    val city: Environment = Environment("Rome")
    writeCityAndVerifyField(robot, city.nameCity)
    robot.clickOn(nextButtonId)
    verifyThat(errorLabel, LabeledMatchers.hasText(""))
    assertEquals(city.nameCity, simulationMVC.simulationController.environment.nameCity)
    assertEquals(city.currentEnvironmentValues, simulationMVC.simulationController.environment.currentEnvironmentValues)
