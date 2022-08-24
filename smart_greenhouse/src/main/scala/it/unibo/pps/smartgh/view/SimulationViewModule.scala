package it.unibo.pps.smartgh.view

import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.component.SelectCityMVC
import it.unibo.pps.smartgh.mvc.{SimulationMVC, component}
import it.unibo.pps.smartgh.view.component.{BaseView, ViewComponent}
import javafx.application.Platform
import javafx.collections.ObservableList
import javafx.scene.Parent
import javafx.scene.image.Image
import javafx.stage.Stage
import scalafx.Includes.*
import scalafx.scene.Scene

/** Object that encloses the view module for the simulation. */
object SimulationViewModule:

  private val appTitle = "Smart Greenhouse"
  private val appSubtitle = "Simulate your smart greenhouse"

  /** A trait that represents the simulation view. */
  trait SimulationView:

    /** The application's base view. */
    val baseView: BaseView

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

    /** Set the text and style of the scene change button.
      * @param styleClass
      *   the style to set
      */
    def changeSceneButtonStyle(styleClass: String): Unit
  /** Trait that represents the provider of the view for the simulation. */
  trait Provider:

    /** The view of the simulation. */
    val simulationView: SimulationView

  /** Trait that represent the view component for the simulation. */
  trait Component:

    /** Implementation of [[SimulationViewModule]]. */
    class SimulationViewImpl(stage: Stage) extends SimulationView:
      private val scene: Scene = Scene(stage.width.value, stage.height.value)
      override val baseView: BaseView = BaseView(appTitle, appSubtitle)

      stage.resizable = true
      stage.maximized = true
      stage.icons.add(Image("images/smartgh.png"))
      stage.title = appTitle

      override def start(simulationMVC: SimulationMVCImpl): Unit =
        val selectCityMVC = component.SelectCityMVC(simulationMVC, baseView)
        baseView.component.setCenter(selectCityMVC.selectCityView)
        baseView.changeSceneButton.getStyleClass.add("normalButton")
        scene.root.value = baseView
        stage.scene = scene
        stage.show()

      override def changeView[A <: Parent](viewComponent: ViewComponent[A]): Unit =
        Platform.runLater(() => baseView.component.setCenter(viewComponent))

      override def changeSceneButtonStyle(styleClass: String): Unit =
        Platform.runLater { () =>
          val styleClasses = baseView.changeSceneButton.getStyleClass
          styleClasses.set(styleClasses.size() - 1, styleClass)
        }
  /** Trait that combine provider and component for the simulation. */
  trait Interface extends Provider with Component
