package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.mvc.MVCCitySearcher
import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.CitySearcherViewModule.CitySearcherView
import javafx.scene.control.TextField
import javafx.scene.layout.{BorderPane, VBox}
import javafx.stage.Stage
import org.junit.jupiter.api.{BeforeAll, Test, TestInstance}
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.matcher.base.NodeMatchers.{isEnabled, isFocused, isVisible}
import org.testfx.matcher.control.{LabeledMatchers, TextInputControlMatchers}
import org.testfx.util.WaitForAsyncUtils
import scalafx.scene.Scene

/** This class contains the tests to verify that the [[SelectCityView]] work correctly. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class CitySearcherViewModuleTest extends AbstractViewTest:

  private var citySearcherView: CitySearcherView = _
  private val textFieldId = "#selectCityTextField"
  private val nextButtonId = "#changeSceneButton"
  private val errorLabel = "#errorLabel"

  @Start
  private def start(stage: Stage): Unit =
    val baseView: BaseView = BaseView(appTitle, appSubtitle)
    citySearcherView = MVCCitySearcher(null, baseView).citySearcherView
    startApplication(stage, baseView, citySearcherView)

  @Test
  def testTextField(robot: FxRobot): Unit =
    verifyThat(textFieldId, isVisible)
    verifyThat(textFieldId, isEnabled)

  @Test
  def testAutoCompletionPopup(robot: FxRobot): Unit =
    val char = "A"
    robot.clickOn(textFieldId)
    robot.write(char)
    verifyThat(textFieldId, TextInputControlMatchers.hasText(char))
    citySearcherView.autoCompletionBinding.getAutoCompletionPopup.getSuggestions.forEach(city =>
      assertTrue(city.startsWith(char))
    )

  @Test
  def testEmptyCityError(robot: FxRobot): Unit =
    verifyThat(textFieldId, TextInputControlMatchers.hasText(""))
    robot.clickOn(nextButtonId)
    verifyThat(errorLabel, isVisible)
    verifyThat(errorLabel, LabeledMatchers.hasText("Please select a city"))

  @Test
  def testWrongCityError(robot: FxRobot): Unit =
    val wrongCity = "Wrong city"
    verifyThat(textFieldId, TextInputControlMatchers.hasText(""))
    robot.clickOn(textFieldId)
    robot.write(wrongCity)
    robot.clickOn(nextButtonId)
    verifyThat(errorLabel, isVisible)
    verifyThat(errorLabel, LabeledMatchers.hasText("The selected city is not valid"))
