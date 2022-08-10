package it.unibo.pps.smartgh.view

import scalafx.scene.Scene
import scalafx.Includes.*
import it.unibo.pps.smartgh.view.component.*
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import it.unibo.pps.smartgh.mvc.{SimulationMVC, component}
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.component.{EnvironmentMVC, SelectCityMVC}
import javafx.application.Platform

/** Object that encloses the view module for the simulation. */
object SimulationViewModule:

  private val appTitle = "Smart Greenhouse"
  private val appSubtitle = "Simula la tua serra intelligente"

  /** A trait that represents the simulation view. */
  trait SimulationView:

    /** Change the current view to the specified one.
      * @param viewComponent
      *   the [[ViewComponent]] to displayed.
      * @tparam A
      *   the type of the root of the component.
      */
    def changeView[A <: Parent](viewComponent: ViewComponent[A]): Unit

    /** Start the application.
      * @param simulationMVC
      *   the simulationMVC of the application.
      */
    def start(simulationMVC: SimulationMVCImpl): Unit

  /** Trait that represents the provider of the view for the simulation. */
  trait Provider:

    /** The view of the simulation. */
    val simulationView: SimulationView

  /** Trait that represent the view component for the simulation. */
  trait Component:

    /** Implementation of [[SimulationViewModule]]. */
    class SimulationViewImpl(stage: Stage) extends SimulationView:
      private val scene: Scene = Scene(stage.width.value, stage.height.value)
      private val baseView: BaseView = BaseView(appTitle, appSubtitle)

      stage.resizable = true
      stage.maximized = true
      stage.title = appTitle

      override def start(simulationMVC: SimulationMVCImpl): Unit =
        val selectCityMVC = component.SelectCityMVC(simulationMVC, baseView)
        baseView.component.setCenter(selectCityMVC.selectCityView) //init view
        scene.root.value = baseView
        stage.scene = scene
        stage.show()

      override def changeView[A <: Parent](viewComponent: ViewComponent[A]): Unit =
        Platform.runLater(() => baseView.component.setCenter(viewComponent))

  /** Trait that combine provider and component for the simulation. */
  trait Interface extends Provider with Component
