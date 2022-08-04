package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import it.unibo.pps.smartgh.controller.EnvironmentControllerModule

import java.text.SimpleDateFormat
import java.util.Calendar

/** Object that can be used to create new instances of [[EnvironmentView]]. */
object EnvironmentViewModule:

  /** A trait that represents the global greenhouse's view of the application. */
  trait EnvironmentView extends ViewComponent[BorderPane]:

    /** View component where it will be possible visualizing the division into areas */
//    val greenHouseDivisionView: GHViewModule.Interface

    /** Data structure that will contains the city's environment values. */
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
    class EnvironmentViewImpl(private val simulationView: SimulationView, private val baseView: BaseView) extends AbstractViewComponent[BorderPane]("ghGlobal.fxml") with EnvironmentView:

      override val component: BorderPane = loader.load[BorderPane]

//      override val greenHouseDivisionView: GHViewModule = GHViewModule()
//      component.setCenter(greenHouseDivisionView)

      val speedSlider: Slider = component.lookup("#timeSpeedSlider").asInstanceOf[Slider]
      speedSlider.setOnMouseReleased(_ => notifySpeedChange(speedSlider.getValue))

      baseView.changeSceneButton.setText("Stop simulation")
      baseView.changeSceneButton.setOnMouseClicked { _ =>
        //todo
        simulationView.changeView(FinishSimulationView(simulationView, baseView))
      }

      val dateFormat: SimpleDateFormat = new SimpleDateFormat("dd / MM / yy")
      val date : String = dateFormat.format(Calendar.getInstance().getTime)
      component.lookup("#setDayLabel").asInstanceOf[Label].setText(date)

      override def setEnvironmentValues(environmentValues: EnvironmentValues): Unit =
        val temperature : String = environmentValues("temp_c").toString
        component.lookup("#setTemperatureLabel").asInstanceOf[Label].setText(temperature)
        val humidity : String = environmentValues("humidity").toString
        component.lookup("#setHumidityLabel").asInstanceOf[Label].setText(humidity)
        val brightness : String = environmentValues("uv").toString
        component.lookup("#setBrightnessLabel").asInstanceOf[Label].setText(brightness)
        val condition : String = environmentValues("condition").toString
        component.lookup("#setConditionLabel").asInstanceOf[Label].setText(condition)

      override def setTimer(timerValue: String): Unit =
        component.lookup("#timeElapsedLabel").asInstanceOf[Label].setText(timerValue)

      private def notifySpeedChange(value: Double): Unit =
        context.controller.timeController.updateVelocityTimer(value)

  trait Interface extends Provider with Component:
    self: Requirements =>