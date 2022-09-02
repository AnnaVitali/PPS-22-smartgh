package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.SimulationViewModule
import javafx.scene.Parent
import javafx.stage.Stage
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{BeforeAll, TestInstance}
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.testfx.util.WaitForAsyncUtils
import scalafx.scene.Scene

/** Abstract class for testing the view. */
abstract class AbstractViewTest:

  @BeforeAll
  def setup(): Unit =
    System.setProperty("testfx.robot", "glass")
    System.setProperty("testfx.headless", "true")
    System.setProperty("java.awt.headless", "true")
    System.setProperty("prism.order", "sw")
    System.setProperty("prism.text", "t2k")
    WaitForAsyncUtils.checkAllExceptions = false
    WaitForAsyncUtils.autoCheckException = false
