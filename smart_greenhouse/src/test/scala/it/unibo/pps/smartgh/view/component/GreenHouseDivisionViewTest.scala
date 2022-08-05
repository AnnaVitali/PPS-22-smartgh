package it.unibo.pps.smartgh.view.component

import com.sun.javafx.css.StyleClassSet.getStyleClass
import it.unibo.pps.smartgh.controller.GHControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.mvc.GreenHouseDivisionMVC
import it.unibo.pps.smartgh.view.component.*
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.stage.Stage
import org.junit.jupiter.api.Assertions.{assertFalse, assertTrue}
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.{BeforeAll, TestInstance}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, ApplicationTest, Start}
import org.testfx.matcher.base.NodeMatchers.{hasChildren, isVisible}
import org.testfx.matcher.control.ButtonMatchers
import org.testfx.matcher.control.LabeledMatchers.hasText
import org.testfx.util.WaitForAsyncUtils

/** This class contains the tests realized to verify the correct behavior of [[GreenHouseDivisionView]]. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class GreenHouseDivisionViewTest extends AbstractViewTest:

  val ghMVC = GreenHouseDivisionMVC(List(Plant("lemon", "citrus limon"), Plant("mint", "mentha x gracilis")))
  val globalGH = "#ghDivision"
  val areaBt = "#areaBt"

  @Start
  private def start(stage: Stage): Unit =
    val baseView: BaseView = BaseView(appTitle, appSubtitle)
    startApplication(stage, baseView, ghMVC.ghDivisionView)
    ghMVC.setAreas()
    ghMVC.show()


  @Test def testLabels(robot: FxRobot): Unit =
    verifyThat(globalGH, isVisible)
    verifyThat(globalGH, hasChildren(ghMVC.ghDivisionModel.plants.length, ""))

  @Test def testStartAreaColor(robot: FxRobot): Unit =
    assertTrue(robot.lookup("#areaBt").queryButton.getStyle.contains("#33cc33"))

  @Test def testChangeAreaColor(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    Thread.sleep(1000)
    assertFalse(robot.lookup("#areaBt").queryButton.getStyle.contains("#33cc33"))

  @Test def testChangeAreaColorMultiple(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    Thread.sleep(1000)
    assertTrue(robot.lookup("#areaBt").queryButton.getStyle.contains("#cc3333"))
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.NORMAL)
    Thread.sleep(6000)
    assertTrue(robot.lookup("#areaBt").queryButton.getStyle.contains("#33cc33"))


  @Test def testStopListening(robot: FxRobot): Unit =
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.ALARM)
    Thread.sleep(5000)
    ghMVC.ghController.stopListening()
    Thread.sleep(5000)
    ghMVC.ghDivisionModel.areas.foreach(a => a.areaModel.status = AreaModelModule.AreaStatus.NORMAL)
    assertFalse(robot.lookup("#areaBt").queryButton.getStyle.contains("#33cc33"))