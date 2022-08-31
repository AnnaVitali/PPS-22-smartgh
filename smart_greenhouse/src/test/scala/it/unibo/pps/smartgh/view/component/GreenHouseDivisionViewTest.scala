package it.unibo.pps.smartgh.view.component

import com.sun.javafx.css.StyleClassSet.getStyleClass
import it.unibo.pps.smartgh.controller.component.GHControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.{AreaModel, AreaStatus}
import it.unibo.pps.smartgh.model.greenhouse.{Environment, GHModelModule}
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.model.time.Timer
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.{EnvironmentMVC, GreenHouseDivisionMVC}
import it.unibo.pps.smartgh.view.component.*
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import monix.execution.Scheduler.Implicits.global
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject
import org.apache.commons.lang3.time.DurationFormatUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.{assertFalse, assertTrue}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{BeforeAll, Test, TestInstance}
import org.scalactic.TripleEquals.convertToEqualizer
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
  var areaModel: AreaModel = _
  val globalGH = "#env"
  val areaBt = "#areaBt"
  val normalStateClassStyle = "NORMALState"
  val alarmStateClassStyle = "ALARMState"

  @Start
  private def start(stage: Stage): Unit =
    val simulationMVC = SimulationMVC(stage)
    simulationMVC.simulationController.environment = Environment("Rome", "41.8931", "12.4828")
    simulationMVC.simulationController.plantsSelected =
      List(Plant("lemon", "citrus limon"), Plant("mint", "mentha x gracilis"))
    simulationMVC.simulationController.startSimulationTimer()
    ghMVC = GreenHouseDivisionMVC(simulationMVC.simulationController.plantsSelected, simulationMVC)
    simulationMVC.simulationView.start(ghMVC.ghDivisionView)

    ghMVC.setAreas(Map.empty)
    areaModel = ghMVC.ghDivisionModel.areas.headOption.orNull.areaModel
    ghMVC.show()

  @Test def testLabels(robot: FxRobot): Unit =
    eventually(timeout(Span(5000, Milliseconds))) {
      verifyThat(globalGH, isVisible)
      verifyThat(areaBt, isVisible)
      verifyThat(globalGH, hasChildren(ghMVC.ghDivisionModel.areas.length, ""))
    }

  @Test def testStartAreaColor(robot: FxRobot): Unit =
    val initial =
      if areaModel.sensors.forall(_.status === SensorStatus.NORMAL) then normalStateClassStyle
      else alarmStateClassStyle
    eventually(timeout(Span(10000, Milliseconds))) {
      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(initial))
    }

  @Test def testChangeAreaColor(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(_.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    eventually(timeout(Span(10000, Milliseconds))) {
      assertFalse(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle))
    }

  @Test def testChangeAreaColorMultiple(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(_.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    eventually(timeout(Span(10000, Milliseconds))) {
      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(alarmStateClassStyle))
    }
    ghMVC.ghDivisionModel.areas.foreach(_.areaModel.status = AreaModelModule.AreaStatus.NORMAL)
    eventually(timeout(Span(10000, Milliseconds))) {
      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle))
    }

  @Test def testStopListening(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(_.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    Thread.sleep(5000)
    ghMVC.ghDivisionController.stopListening()
    Thread.sleep(5000)
    ghMVC.ghDivisionModel.areas.foreach(_.areaModel.status = AreaModelModule.AreaStatus.NORMAL)
    eventually(timeout(Span(10000, Milliseconds))) {
      assertFalse(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle))
    }

  private def areaStateWithAllOtherNORMAL(): Unit =
    areaModel.sensors.foreach { s =>
      if s.name !== "Brightness" then
        s.status = SensorStatus.NORMAL
        s.actualVal = s.max
    }
    areaModel.status =
      if areaModel.sensors.forall(_.status === SensorStatus.NORMAL) then AreaStatus.NORMAL else AreaStatus.ALARM

  @Test def testAreaChangeStatusWithSensor(robot: FxRobot): Unit =
    import it.unibo.pps.smartgh.model.area.AreaGatesState
    import it.unibo.pps.smartgh.model.sensor.SensorStatus
    import monix.execution.Scheduler.Implicits.global
    val subjectEnvironment: ConcurrentSubject[Double, Double] =
      ConcurrentSubject[Double](MulticastStrategy.publish)
    areaModel.setSensorSubjects(Map("lux" -> subjectEnvironment))

    areaModel.updGateState(AreaGatesState.Close)
    areaModel.updBrightnessOfLamp(5000)
    Thread.sleep(3000)
    eventually(timeout(Span(10000, Milliseconds))) {
      areaStateWithAllOtherNORMAL()
      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle)) //INITIAL STATUS
    }

    areaModel.updBrightnessOfLamp(0)
    eventually(timeout(Span(10000, Milliseconds))) {
      areaStateWithAllOtherNORMAL()
      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(alarmStateClassStyle)) //ALARM STATUS
    }

    areaModel.updBrightnessOfLamp(5000)
    eventually(timeout(Span(10000, Milliseconds))) {
      areaStateWithAllOtherNORMAL()
      assertTrue(robot.lookup(areaBt).queryButton.getStyleClass.contains(normalStateClassStyle)) //NORMAL STATUS
    }
