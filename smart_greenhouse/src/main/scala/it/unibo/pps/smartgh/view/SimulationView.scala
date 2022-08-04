package it.unibo.pps.smartgh.view

import it.unibo.pps.smartgh.mvc.SelectCityMVC
import scalafx.scene.Scene
import scalafx.Includes.*
import it.unibo.pps.smartgh.view.component.*
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

/** The view of the application. */
trait SimulationView:

  /** Change the current view to the specified one.
    * @param viewComponent
    *   the [[ViewComponent]] to displayed
    * @tparam A
    *   the type of the component
    */
  def changeView[A <: Parent](viewComponent: ViewComponent[A]): Unit

/** Object that can be used to create new instances of [[SimulationView]]. */
object SimulationView:
  private val appTitle = "Smart Greenhouse"
  private val appSubtitle = "Simula la tua serra intelligente"

  /** Creates a new [[SimulationView]] for creating a view of the application.
    * @param stage
    *   the ScalaFX [[Stage]] used for creating the view
    * @return
    *   a new instance of [[SimulationView]]
    */
  def apply(stage: Stage): SimulationView = SimulationViewImpl(stage)

  /** Implementation of [[SimulationView]]. */
  private class SimulationViewImpl(stage: Stage) extends SimulationView:
    private val scene: Scene = Scene(stage.width.value, stage.height.value)
    private val baseView: BaseView = BaseView(appTitle, appSubtitle)

    stage.resizable = true
    stage.maximized = true
    stage.title = appTitle
    baseView.component.setCenter(SelectCityMVC(this, baseView).selectCityView) //init view
    scene.root.value = baseView
    stage.scene = scene
    stage.show()

    override def changeView[A <: Parent](viewComponent: ViewComponent[A]): Unit =
      baseView.component.setCenter(viewComponent)

//      val viewChildren = baseView.getChildren
//      viewChildren.set(viewChildren.size() - 1, viewComponent)
