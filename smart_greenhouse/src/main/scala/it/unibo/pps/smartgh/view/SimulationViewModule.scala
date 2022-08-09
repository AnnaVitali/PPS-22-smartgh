package it.unibo.pps.smartgh.view

import it.unibo.pps.smartgh.mvc.SelectCityMVC
import scalafx.scene.Scene
import scalafx.Includes.*
import it.unibo.pps.smartgh.view.component.*
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import it.unibo.pps.smartgh.mvc.EnvironmentMVC
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl

/** The view of the application. */
object SimulationViewModule:

  private val appTitle = "Smart Greenhouse"
  private val appSubtitle = "Simula la tua serra intelligente"

  trait SimulationView:

    /** Change the current view to the specified one.
      * @param viewComponent
      *   the [[ViewComponent]] to displayed
      * @tparam A
      *   the type of the component
      */
    def changeView[A <: Parent](viewComponent: ViewComponent[A]): Unit

    def start(simulationMVC: SimulationMVCImpl) : Unit

  trait Provider:
    val simulationView: SimulationView

  trait Component:
    /** Implementation of [[SimulationViewModule]]. */
    class SimulationViewImpl(stage: Stage) extends SimulationView:
      private val scene: Scene = Scene(stage.width.value, stage.height.value)
      private val baseView: BaseView = BaseView(appTitle, appSubtitle)

      stage.resizable = true
      stage.maximized = true
      stage.title = appTitle

      override def start(simulationMVC: SimulationMVCImpl): Unit =
        val selectCityMVC = SelectCityMVC(simulationMVC, baseView)
        baseView.component.setCenter(selectCityMVC.selectCityView) //init view
        scene.root.value = baseView
        stage.scene = scene
        stage.show()

      override def changeView[A <: Parent](viewComponent: ViewComponent[A]): Unit =
        baseView.component.setCenter(viewComponent)

  trait Interface extends Provider with Component
