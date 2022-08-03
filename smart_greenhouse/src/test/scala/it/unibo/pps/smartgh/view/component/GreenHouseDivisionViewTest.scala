package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.GHControllerModule
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.mvc.GreenHouseDivisionMVC.controller
import it.unibo.pps.smartgh.view.component.*
import javafx.scene.layout.VBox
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.{BeforeAll, TestInstance}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, ApplicationTest, Start}
import org.testfx.matcher.base.NodeMatchers.{hasChildren, isVisible}
import org.testfx.matcher.control.LabeledMatchers.hasText
import org.testfx.util.WaitForAsyncUtils
import scalafx.scene.Scene

/** This class contains the tests realized to verify the correct behavior of [[GreenHouseDivisionView]]. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class GreenHouseDivisionViewTest extends AbstractViewTest:
  import it.unibo.pps.smartgh.mvc.GreenHouseDivisionMVC
  val globalGH = "#ghDivision"


  @Start
  private def start(stage: Stage): Unit =
    val baseView: BaseView = BaseView(appTitle, appSubtitle)
    startApplication(stage, baseView, GreenHouseDivisionMVC.view)


  @Test def testLabels(robot: FxRobot): Unit =

    verifyThat(globalGH, isVisible())
    verifyThat(globalGH, hasChildren(GreenHouseDivisionMVC.model.plants.length, ""))
