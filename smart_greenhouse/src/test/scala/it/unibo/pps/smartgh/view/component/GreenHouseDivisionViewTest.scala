package it.unibo.pps.smartgh.view.component

import com.sun.javafx.css.StyleClassSet.getStyleClass
import it.unibo.pps.smartgh.controller.component.GHControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaStatus
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.{BeforeAll, TestInstance}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.concurrent.Futures.timeout
import org.scalatest.time.{Milliseconds, Span}
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
//    Thread.sleep(10000)

  @Test def testLabels(robot: FxRobot): Unit =
    eventually(timeout(Span(3000, Milliseconds))) {
      verifyThat(globalGH, isVisible)
      verifyThat(areaBt, isVisible)
      verifyThat(globalGH, hasChildren(ghMVC.ghDivisionModel.plants.length, ""))
    }

  @Test def testStartAreaColor(robot: FxRobot): Unit =
    eventually(timeout(Span(3000, Milliseconds))) {
      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle))
    }

  @Test def testChangeAreaColor(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    eventually(timeout(Span(3000, Milliseconds))) {
      assertFalse(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle))
    }

  @Test def testChangeAreaColorMultiple(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    eventually(timeout(Span(3000, Milliseconds))) {
      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(alarmStateClassStyle))
    }
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.NORMAL)
    eventually(timeout(Span(3000, Milliseconds))) {
      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle))
    }

  @Test def testStopListening(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    Thread.sleep(5000)
    ghMVC.ghController.stopListening()
    Thread.sleep(5000)
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.NORMAL)
    eventually(timeout(Span(3000, Milliseconds))) {
      assertFalse(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle))
    }

//  @Test def testAreaChangeStatusWithSensor(robot: FxRobot): Unit =
//    import monix.execution.Scheduler.Implicits.global
//    import it.unibo.pps.smartgh.model.sensor.SensorStatus
//    import it.unibo.pps.smartgh.model.area.AreaGatesState
//    val areaModel = ghMVC.ghDivisionModel.areas.head.areaModel
//    areaModel.updBrightnessOfLamp(0)
//    val subjectEnvironment: ConcurrentSubject[Double, Double] =
//      ConcurrentSubject[Double](MulticastStrategy.publish)
//    areaModel.setSensorSubjects(Map("lux" -> subjectEnvironment))
//    areaModel.updGateState(AreaGatesState.Close)
//    areaModel.sensors.foreach { s =>
//      s.status = SensorStatus.NORMAL
//      s.actualVal = s.max - 1.0
//    }
//    subjectEnvironment.onNext(20000)
//    Thread.sleep(3000)
//    val initialState = areaModel.status match
//      case AreaStatus.NORMAL => normalStateClassStyle
//      case _ => alarmStateClassStyle
//    areaModel.sensors.foreach(s => println(s.name + s.status))
//    eventually(timeout(Span(8000, Milliseconds))) {
//      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(initialState)) //INITIAL STATUS
//    }
//
//    subjectEnvironment.onNext(0)
//    eventually(timeout(Span(8000, Milliseconds))) {
//      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(alarmStateClassStyle)) //ALARM STATUS
//    }
//
//    areaModel.updBrightnessOfLamp(5000)
////    subjectEnvironment.onNext(5000)
//    eventually(timeout(Span(10000, Milliseconds))) {
//      areaModel.sensors.foreach(s => println(s.name + s.status))
//      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle)) //NORMAL STATUS
//    }
