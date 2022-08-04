package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.scene.control.*
import javafx.scene.layout.BorderPane

object EnvironmentViewModule:

  /** A trait that represents the global greenhouse's view of the application. */
  trait EnvironmentView extends ViewComponent[BorderPane]:

    /** View component where it will be possible visualizing the division into areas */
    val greenHouseDivisionView: GHViewModule.Interface
  
    type EnvironmentValues = Map[String, Any]

    /** Method to update the view of environment's values
      * @param environmentValues
      *   environment's values relating to the city selected by the user
      */
    def setEnvironmentValues(environmentValues: EnvironmentValues): Unit

    /** Method to update the view of elapsed simulation time
      * @param timerValue
      *   current value of the simulation timer
      */
    def setTimer(timerValue: String): Unit

  trait Provider:
    val view : EnvironmentView

  type Requirements = EnvironmentControllerModule.Provider

  trait Component:
    context: Requirements =>
    class EnvironmentViewImpl() extends AbstractViewComponent[BorderPane]("ghGlobal.fxml") with EnvironmentView:

      override val component: BorderPane = loader.load[BorderPane]

      override val greenHouseDivisionView: GHViewModule = GHViewModule()
      component.setCenter(greenHouseDivisionView)

      val timeElapsedLabel: Label = component.lookup("#timeElapsedLabel").asInstanceOf[Label]

      val speedSlider: Slider = component.lookup("#timeSpeedSlider").asInstanceOf[Slider]
      speedSlider.setOnMouseReleased(_ => notifySpeedChange(speedSlider.getValue))

      baseView.changeSceneButton.setText("Stop simulation")
      baseView.changeSceneButton.setOnMouseClicked { _ =>
        //todo
        simulationView.changeView(FinishSimulationView(simulationView, baseView))
      }

      override def setEnvironmentValues(environmentValues: EnvironmentValues): Unit = ???

      override def setTimer(timerValue: String): Unit =
        timeElapsedLabel.setText(timerValue)

      private def notifySpeedChange(value: Double): Unit =
      // todo: richiamare metodo changeSpeed di TimeModel
        ???

  trait Interface extends Provider with Component:
    self: Requirements =>