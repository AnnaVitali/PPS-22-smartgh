package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.SimulationView
import javafx.scene.Parent
import javafx.stage.Stage
import org.junit.jupiter.api.{BeforeAll, TestInstance}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.testfx.util.WaitForAsyncUtils
import scalafx.scene.Scene

abstract class AbstractViewTest:

  protected var simulationView: SimulationView = _
  protected val appTitle = "Smart Greenhouse"
  protected val appSubtitle = "Simula la tua serra intelligente"

  @BeforeAll
  def setup(): Unit =
    System.setProperty("testfx.robot", "glass")
    System.setProperty("testfx.headless", "true")
    System.setProperty("java.awt.headless", "true")
    System.setProperty("prism.order", "sw")
    System.setProperty("prism.text", "t2k")
    WaitForAsyncUtils.checkAllExceptions = false
    WaitForAsyncUtils.autoCheckException = false

  def startApplication(stage: Stage, baseView: BaseView, viewComponent: ViewComponent[? <: Parent]): Unit =
    val scene: Scene = Scene(stage.getWidth, stage.getHeight)
    stage.setResizable(true)
    stage.setMaximized(true)
    baseView.component.setCenter(viewComponent)
    scene.root.value = baseView
    stage.setScene(scene)
    stage.show()
