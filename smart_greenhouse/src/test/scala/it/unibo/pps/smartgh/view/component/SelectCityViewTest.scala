package it.unibo.pps.smartgh.view.component

import javafx.scene.control.TextField
import javafx.scene.layout.{Pane, VBox}
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
class SelectCityViewTest:

  private var selectCityView: SelectCityView = _
  private val textFieldId = "#selectCityTextField"
  private val nextButtonId = "#nextButton" //todo: cambiare l'id giusto quando mettiamo in comune il bottone
  private val errorLabel = "#errorLabel"

  @BeforeAll
  def setup(): Unit =
    System.setProperty("testfx.robot", "glass")
    System.setProperty("testfx.headless", "true")
    System.setProperty("java.awt.headless", "true")
    System.setProperty("prism.order", "sw")
    System.setProperty("prism.text", "t2k")
    WaitForAsyncUtils.checkAllExceptions = false;
    WaitForAsyncUtils.autoCheckException = false;

  @Start
  private def start(stage: Stage): Unit =
    val scene: Scene = Scene(stage.getWidth, stage.getHeight)
    val baseView: ViewComponent[VBox] = BaseView("title", "subtitle")
    selectCityView = SelectCityView()
    stage.setResizable(true)
    baseView.getChildren.add(selectCityView)
    scene.root.value = baseView
    stage.setScene(scene)
    stage.show()

  @Test
  def testTextField(robot: FxRobot): Unit =
    verifyThat(textFieldId, isVisible)
    verifyThat(textFieldId, isEnabled)
    verifyThat(textFieldId, isFocused)

  @Test
  def testAutoCompletionPopup(robot: FxRobot): Unit =
    val char = "A"
    robot.write(char)
    verifyThat(textFieldId, TextInputControlMatchers.hasText(char))
    selectCityView.autoCompletionBinding.getAutoCompletionPopup.getSuggestions.forEach(city =>
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
    robot.write(wrongCity)
    robot.clickOn(nextButtonId)
    verifyThat(errorLabel, isVisible)
    verifyThat(errorLabel, LabeledMatchers.hasText("The selected city is not valid"))
