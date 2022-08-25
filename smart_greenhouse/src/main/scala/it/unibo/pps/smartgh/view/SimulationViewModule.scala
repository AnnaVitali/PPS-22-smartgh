package it.unibo.pps.smartgh.view

import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.component.{BaseView, ViewComponent}
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.scene.Parent
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.stage.Stage
import scalafx.Includes.*
import scalafx.scene.Scene

/** Object that encloses the view module for the simulation. */
object SimulationViewModule:

  private val AppTitle = "Smart Greenhouse"
  private val AppSubtitle = "Simulate your smart greenhouse"

  /** A trait that represents the simulation view. */
  trait SimulationView:

    /** Change the current view to the specified one.
      * @param viewComponent
      *   the [[ViewComponent]] to displayed.
      */
    def changeView(viewComponent: ViewComponent[_ <: Parent]): Unit

    /** Start the application.
      * @param viewComponent
      *   the first component to show.
      */
    def start(viewComponent: ViewComponent[_ <: Parent]): Unit

    /** Set the text of the change scene button.
      * @param text
      *   the text to set.
      * @param eventHandler
      *   the mouse event handler to be set on the button.
      * @param visibility
      *   the visibility of the button.
      */
    def changeSceneButtonBehaviour(
        text: String = "",
        eventHandler: EventHandler[_ >: MouseEvent] = _ => {},
        visibility: Boolean = true
    ): Unit

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
    class SimulationViewImpl(private val stage: Stage) extends SimulationView:
      private val scene: Scene = Scene(stage.width.value, stage.height.value)
      private val baseView: BaseView = BaseView(AppTitle, AppSubtitle)

      stage.resizable = true
      stage.maximized = true
      stage.icons.add(Image("images/smartgh.png"))
      stage.title = AppTitle

      override def start(viewComponent: ViewComponent[_ <: Parent]): Unit =
        baseView.component.setCenter(viewComponent)
        baseView.changeSceneButton.getStyleClass.add("normalButton")
        scene.root.value = baseView
        stage.scene = scene
        stage.show()

      override def changeView(viewComponent: ViewComponent[_ <: Parent]): Unit =
        Platform.runLater(() => baseView.component.setCenter(viewComponent))

      override def changeSceneButtonBehaviour(
          text: String,
          eventHandler: EventHandler[_ >: MouseEvent],
          visibility: Boolean
      ): Unit =
        Platform.runLater { () =>
          baseView.changeSceneButton.setText(text)
          baseView.changeSceneButton.setOnMouseClicked(eventHandler)
          baseView.changeSceneButton.setVisible(visibility)
        }

      override def changeSceneButtonStyle(styleClass: String): Unit =
        Platform.runLater { () =>
          val styleClasses = baseView.changeSceneButton.getStyleClass
          styleClasses.set(styleClasses.size() - 1, styleClass)
        }
  /** Trait that combine provider and component for the simulation. */
  trait Interface extends Provider with Component
