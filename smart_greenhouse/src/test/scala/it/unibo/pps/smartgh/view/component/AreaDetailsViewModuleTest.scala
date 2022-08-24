package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaStatus.NORMAL
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{Test, TestInstance}
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.concurrent.Futures.timeout
import org.scalatest.time.{Milliseconds, Span}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.testfx.matcher.base.NodeMatchers.isVisible
import org.testfx.matcher.control.LabeledMatchers.hasText

/** This class contains the tests to verify that the [[AreaDetailsViewModule]] work correctly. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class AreaDetailsViewModuleTest extends AbstractAreaDetailsViewTest:

  private val plantNameId = "#plantNameLabel"
  private val plantDescriptionId = "#plantDescriptionLabel"
  private val plantImageUrlId = "#plantImage"
  private val timerId = "#timerLabel"
  private val stateId = "#statusLabel"
  private val alarmPaneId = "#alarmPane"
  private val alarmMsg = "#alarmLabel"

  @Start
  override def start(stage: Stage): Unit = super.start(stage)

  @Test
  def testPlantInformation(robot: FxRobot): Unit =
    val image = robot.lookup(plantImageUrlId).queryAs(classOf[ImageView])
    eventually(timeout(Span(5000, Milliseconds))) {
      verifyThat(plantNameId, hasText(plant.name))
      verifyThat(plantDescriptionId, hasText(plant.description))
      assertEquals(plant.imageUrl, image.getImage.getUrl)
      assertFalse(image.getImage.isError)
    }

  @Test
  def testTimerStarted(robot: FxRobot): Unit =
    val timerLabel = robot.lookup(timerId).queryLabeled()
    eventually(timeout(Span(5000, Milliseconds))) {
      assertFalse(timerLabel.getText.isBlank)
      assertTrue(timerLabel.getText.takeRight(2).toInt > 0)
    }

  @Test
  def testStatus(robot: FxRobot): Unit =
    val stateLabel = robot.lookup(stateId).queryLabeled()
    val alarmPane = robot.lookup(alarmPaneId).queryParent()
    val alarmLabel = robot.lookup(alarmMsg).queryLabeled()
    assertFalse(stateLabel.getText.isBlank)
    areaDetailsMVC.areaModel.status match
      case NORMAL =>
        eventually(timeout(Span(10000, Milliseconds))) {
          verifyThat(stateId, hasText("NORMAL"))
          assertFalse(alarmPane.isVisible)
          assertTrue(alarmLabel.getText.isBlank)
        }
      case _ =>
        eventually(timeout(Span(10000, Milliseconds))) {
          verifyThat(stateId, hasText("ALARM"))
          verifyThat(alarmPane, isVisible)
          assertFalse(alarmLabel.getText.isBlank)
        }
