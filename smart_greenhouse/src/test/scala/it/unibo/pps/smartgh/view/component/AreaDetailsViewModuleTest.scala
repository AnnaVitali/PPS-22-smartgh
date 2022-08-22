package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaStatus
import it.unibo.pps.smartgh.model.greenhouse.Environment
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.time.Timer
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.AreaDetailsMVC.AreaDetailsMVCImpl
import it.unibo.pps.smartgh.mvc.component.{AreaDetailsMVC, AreaMVC, EnvironmentMVC, GreenHouseDivisionMVC}
import it.unibo.pps.smartgh.view.component.AreaDetailsViewModule.AreaDetailsView
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.stage.Stage
import org.apache.commons.lang3.time.DurationFormatUtils
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

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import scala.concurrent.duration.DurationInt

/** This class contains the tests to verify that the [[AreaDetailsViewModule]] work correctly. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class AreaDetailsViewModuleTest extends AbstractViewTest:

  protected var areaDetailsMVC: AreaDetailsMVCImpl = _
  private val environment = Environment("Rome")
  private val plant = Plant("lemon", "citrus limon")
  private val plantNameId = "#plantNameLabel"
  private val plantDescriptionId = "#plantDescriptionLabel"
  private val plantImageUrlId = "#plantImage"
  private val timerId = "#timerLabel"
  private val stateId = "#statusLabel"
  private val alarmPaneId = "#alarmPane"
  private val alarmMsg = "#alarmLabel"

  @Start
  def start(stage: Stage): Unit =
    val baseView = BaseView(appTitle, appSubtitle)
    simulationMVC = SimulationMVC(stage)
    simulationMVC.simulationController.startSimulationTimer()
    simulationMVC.simulationController.plantsSelected = List(plant)
    val environmentMVC = EnvironmentMVC(simulationMVC, baseView)
    simulationMVC.simulationController.environment = environment
    simulationMVC.simulationController.environmentController = environmentMVC.environmentController
    val greenHouseMVC = GreenHouseDivisionMVC(List(plant), simulationMVC)
    val areaMCV = AreaMVC(plant, simulationMVC, greenHouseMVC)
    areaDetailsMVC = AreaDetailsMVC(simulationMVC, baseView, areaMCV.areaModel)
    startApplication(stage, baseView, areaDetailsMVC.areaDetailsView)

  @Test
  def testPlantInformation(robot: FxRobot): Unit =
    val image = robot.lookup(plantImageUrlId).queryAs(classOf[ImageView])
    verifyThat(plantNameId, hasText(plant.name.capitalize))
    verifyThat(plantDescriptionId, hasText(plant.description))
    assertEquals(plant.imageUrl, image.getImage.getUrl)
    assertFalse(image.getImage.isError)

  @Test
  def testTimerStarted(robot: FxRobot): Unit =
    val timerLabel = robot.lookup(timerId).queryLabeled()
    eventually(timeout(Span(3000, Milliseconds))) {
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
      case AreaStatus.NORMAL =>
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
