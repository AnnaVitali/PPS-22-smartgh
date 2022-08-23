package it.unibo.pps.smartgh.view.component

import com.sun.javafx.css.StyleClassSet.getStyleClass
import it.unibo.pps.smartgh.controller.component.GHControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.greenhouse.{Environment, GHModelModule}
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.time.Timer
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.GreenHouseDivisionMVC
import it.unibo.pps.smartgh.view.component.*
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.{assertFalse, assertTrue}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{BeforeAll, Test, TestInstance}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, ApplicationTest, Start}
import org.testfx.matcher.base.NodeMatchers.{hasChildren, isVisible}
import org.testfx.matcher.control.ButtonMatchers
import org.testfx.matcher.control.LabeledMatchers.hasText
import org.testfx.util.WaitForAsyncUtils

import scala.concurrent.duration.*
import scala.language.postfixOps

/** This class contains the tests realized to verify the correct behavior of [[GreenHouseDivisionView]]. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class GreenHouseDivisionViewTest extends AbstractViewTest:

  var ghMVC: GreenHouseDivisionMVC.GreenHouseDivisionMVCImpl = _
  val globalGH = "#env"
  val areaBt = "#areaBt"
  val normalStateClassStyle = "NORMALState"
  val alarmStateClassStyle = "ALARMState"
  private val timer = Timer(1 day)
  timer.start(println("time is up!"))

  @Start
  private def start(stage: Stage): Unit =
    val baseView: BaseView = BaseView(appTitle, appSubtitle)
    simulationMVC = SimulationMVC(stage)
    simulationMVC.simulationController.environment = Environment("Cesena")
    ghMVC = GreenHouseDivisionMVC(
      List(Plant("lemon", "citrus limon"), Plant("mint", "mentha x gracilis")),
      simulationMVC
    )
    startApplication(stage, baseView, ghMVC.ghDivisionView)
    ghMVC.setAreas(timer, Map.empty)
    ghMVC.show()
    Thread.sleep(10000)

  @Test def testLabels(robot: FxRobot): Unit =
    verifyThat(globalGH, isVisible)
    verifyThat(areaBt, isVisible)
    verifyThat(globalGH, hasChildren(ghMVC.ghDivisionModel.plants.length, ""))

  @Test def testStartAreaColor(robot: FxRobot): Unit =
    assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle))

  @Test def testChangeAreaColor(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    Thread.sleep(1000)
    assertFalse(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle))

  @Test def testChangeAreaColorMultiple(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    Thread.sleep(1000)
    assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(alarmStateClassStyle))
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.NORMAL)
    Thread.sleep(6000)
    assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle))

  @Test def testStopListening(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    Thread.sleep(5000)
    ghMVC.ghController.stopListening()
    Thread.sleep(5000)
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.NORMAL)
    assertFalse(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle))

  @Test def testAreaChangeStatusWithSensor(robot: FxRobot): Unit =
    import it.unibo.pps.smartgh.model.area.AreaGatesState
    import it.unibo.pps.smartgh.model.sensor.SensorStatus
    import monix.execution.Scheduler.Implicits.global
    val areaModel = ghMVC.ghDivisionModel.areas.head.areaModel
    areaModel.updBrightnessOfLamp(0)
    val subjectEnvironment: ConcurrentSubject[Double, Double] =
      ConcurrentSubject[Double](MulticastStrategy.publish)
    areaModel.setSensorSubjects(Map("lux" -> subjectEnvironment))

    subjectEnvironment.onNext(20000)
    Thread.sleep(5000)
    assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle)) //NORMAL STATUS

    subjectEnvironment.onNext(0)
    Thread.sleep(8000)
    assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(alarmStateClassStyle)) //ALARM STATUS

    subjectEnvironment.onNext(10000)
    Thread.sleep(5000)
    assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle)) //NORMAL STATUS
