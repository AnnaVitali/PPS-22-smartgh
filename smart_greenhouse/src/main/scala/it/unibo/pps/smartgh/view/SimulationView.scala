package it.unibo.pps.smartgh.view

import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes.*
import it.unibo.pps.smartgh.view.component.{BaseView, SelectCityView, ViewComponent}
import javafx.scene.Parent
import javafx.scene.layout.{Pane, VBox}
import scalafx.application.Platform

trait SimulationView:
  def changeView[A <: Parent](viewComponent: ViewComponent[A]): Unit

object SimulationView:
  private val appTitle = "Smart Greenhouse"
  private val appSubtitle = "Simula la tua serra intelligente"

  def apply(stage: PrimaryStage): SimulationView = SimulationViewImpl(stage)

  private class SimulationViewImpl(stage: PrimaryStage) extends SimulationView:
    private val scene: Scene = Scene(stage.width.value, stage.height.value)
    private val baseView: ViewComponent[VBox] = BaseView(this, appTitle, appSubtitle)

    stage.resizable = true
    stage.maximized = true
    stage.title = appTitle
    baseView.getChildren.add(SelectCityView()) //init view
    scene.root.value = baseView
    stage.scene = scene
    stage.show()

    override def changeView[A <: Parent](viewComponent: ViewComponent[A]): Unit =
      val viewChildren = baseView.getChildren
      viewChildren.set(viewChildren.size() - 1, viewComponent)
