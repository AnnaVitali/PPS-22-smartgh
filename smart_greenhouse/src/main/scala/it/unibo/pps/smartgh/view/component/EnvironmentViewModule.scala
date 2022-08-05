package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import it.unibo.pps.smartgh.controller.EnvironmentControllerModule
import it.unibo.pps.smartgh.mvc.GreenHouseDivisionMVC

import java.text.SimpleDateFormat
import java.util.Calendar
import javafx.application.Platform

import scala.language.postfixOps

/** Object that can be used to create new instances of [[EnvironmentView]]. */
object EnvironmentViewModule:

  /** A trait that represents the global greenhouse's view of the application. */
  trait EnvironmentView extends ViewComponent[BorderPane]:

    /** View component where it will be possible visualizing the division into areas */
//    val greenHouseDivisionView: GHViewModule.Interface

    /** Data structure that will contains the city's environment values. */
    type EnvironmentValues = Map[String, Any]

    def displayNameCity(cityName : String) : Unit

    /** Method to update the view of environment's values
      * @param environmentValues
      *   environment's values relating to the city selected by the user
      */
    def displayEnvironmentValues(environmentValues: EnvironmentValues): Unit

    /** Method to update the view of elapsed simulation time
      * @param timerValue
      *   current value of the simulation timer
      */
    def displayElapsedTime(timerValue: String): Unit

    def finishSimulation() : Unit

  trait Provider:
    val view : EnvironmentView

  type Requirements = EnvironmentControllerModule.Provider

  trait Component:
    context: Requirements =>
    class EnvironmentViewImpl(private val simulationView: SimulationView, private val baseView: BaseView) extends AbstractViewComponent[BorderPane]("ghGlobal.fxml") with EnvironmentView:

      override val component: BorderPane = loader.load[BorderPane]

      val greenHouseMVC = GreenHouseDivisionMVC
      component.setCenter(greenHouseMVC.view)

      val speedSlider: Slider = component.lookup("#timeSpeedSlider").asInstanceOf[Slider]
      speedSlider.setOnMouseReleased(_ => notifySpeedChange(speedSlider.getValue))

      baseView.changeSceneButton.setText("Stop simulation")
      baseView.changeSceneButton.setOnMouseClicked { _ =>
        context.controller.stopSimulation()
        simulationView.changeView(FinishSimulationView(simulationView, baseView))
      }

      val dateFormat: SimpleDateFormat = new SimpleDateFormat("dd / MM / yy")
      val date : String = dateFormat.format(Calendar.getInstance().getTime)
      component.lookup("#setDayLabel").asInstanceOf[Label].setText(date)

      override def displayNameCity(cityName: String): Unit =
        component.lookup("#setLocationLabel").asInstanceOf[Label].setText(cityName)

      override def displayEnvironmentValues(environmentValues: EnvironmentValues): Unit =
        Platform.runLater(() =>
          val temperature : String = environmentValues("temp_c").toString
          component.lookup("#setTemperatureLabel").asInstanceOf[Label].setText(temperature + "°")
          val humidity : String = environmentValues("humidity").toString
          component.lookup("#setHumidityLabel").asInstanceOf[Label].setText(humidity + "%")
          val brightness : String = environmentValues("uv").toString
          component.lookup("#setBrightnessLabel").asInstanceOf[Label].setText(brightness + " lux")
          val condition : String = environmentValues("condition").toString
          component.lookup("#setConditionLabel").asInstanceOf[Label].setText(condition)
        )

      override def displayElapsedTime(timerValue: String): Unit =
        Platform.runLater(() =>
          component.lookup("#timeElapsedLabel").asInstanceOf[Label].setText(timerValue)
        )

      override def finishSimulation(): Unit =
        Platform.runLater(() =>
          simulationView.changeView(FinishSimulationView(simulationView, baseView))
        )

      private def notifySpeedChange(value: Double): Unit =
        context.controller.timeController.updateVelocityTimer(value)

  trait Interface extends Provider with Component:
    self: Requirements =>