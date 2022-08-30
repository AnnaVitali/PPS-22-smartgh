package it.unibo.pps.smartgh.view.component.areaParameter

import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
import it.unibo.pps.smartgh.view.component.AbstractAreaDetailsViewTest
import javafx.scene.control.{ButtonBase, Label, ToggleButton}
import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{Test, TestInstance}
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.concurrent.Futures.timeout
import org.scalatest.time.{Milliseconds, Span}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.testfx.matcher.base.NodeMatchers.{isEnabled, isVisible}
import org.testfx.matcher.control.LabeledMatchers.hasText

import scala.jdk.javaapi.CollectionConverters.asScala
import scala.reflect.{ClassTag, classTag}

/** An abstract class for creating testing area parameter.
  * @param parameterName
  *   the name of parameter
  * @param sensorName
  *   the name of sensor
  */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
abstract class AbstractAreaParameterViewTest(private val parameterName: String, private val sensorName: String)
    extends AbstractAreaDetailsViewTest:

  protected var sensor: ManageSensorImpl = _
  private val descriptionId = "#descriptionLabel"
  private val currentValueId = "#currentValueLabel"

  private def parameterOptimalValues: String = "(%.2f%s, %.2f%s)".format(sensor.min, sensor.um, sensor.max, sensor.um)

  @Start
  override def start(stage: Stage): Unit =
    super.start(stage)
    sensor = areaDetailsMVC.areaModel.sensors.find(_.name.contentEquals(sensorName)).orNull

  @Test
  def testParameters(robot: FxRobot): Unit =
    val descriptions = asScala(robot.lookup(descriptionId).queryAllAs(classOf[Label])).toSeq
    assertTrue(descriptions.exists(_.getText.contentEquals(parameterName + " " + parameterOptimalValues)))

  @Test
  def testValue(robot: FxRobot): Unit =
    val values = asScala(robot.lookup(currentValueId).queryAllAs(classOf[Label])).toSeq
    eventually(timeout(Span(10000, Milliseconds))) {
      assertTrue(values.exists(_.getText.contentEquals("%.2f %s".format(sensor.actualVal, sensor.um))))
    }

  protected def getToggleButton(robot: FxRobot, buttonId: String): ToggleButton =
    robot.lookup(buttonId).queryAs(classOf[ToggleButton])

  protected def basicToggleButtonTest(button: ToggleButton, buttonId: String, text: String, selected: Boolean): Unit =
    basicButtonTest(button, buttonId, text)
    assertTrue(button.isSelected == selected)

  protected def basicButtonTest(button: ButtonBase, buttonId: String, text: String): Unit =
    verifyThat(buttonId, isVisible)
    verifyThat(buttonId, hasText(text))

  protected def testActions(robot: FxRobot, buttonId: String, condition: (Double, Double) => Boolean): Unit =
    val value = sensor.actualVal
    robot.clickOn(buttonId)

    eventually(timeout(Span(10000, Milliseconds))) {
      assertTrue(condition(sensor.actualVal, value))
    }

    testValue(robot)
