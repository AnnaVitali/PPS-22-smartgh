package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import it.unibo.pps.smartgh.controller.EnvironmentControllerModule
import it.unibo.pps.smartgh.mvc.GreenHouseDivisionMVC
import it.unibo.pps.smartgh.mvc.GreenHouseDivisionMVC.GreenHouseDivisionMVCImpl
import it.unibo.pps.smartgh.view.component.GHViewModule.GHDivisionView
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import javafx.application.Platform
import javafx.fxml.FXML

import scala.language.postfixOps

/** Object that encloses the view module to display ambient environment's values and simulation time. */
object EnvironmentViewModule:

  /** A trait that represents the environment's view of the application. */
  trait EnvironmentView extends ViewComponent[BorderPane]:

    /** Data structure that will contains environment values. */
    type EnvironmentValues = Map[String, Any]

    /** Method to dislay the selected city's name in location label.
      * @param cityName
      *   selected city's name
      */
    def displayNameCity(cityName: String): Unit

    /** Method to update the view of environment values.
      * @param environmentValues
      *   environment values related to the city selected by the user
      */
    def displayEnvironmentValues(environmentValues: EnvironmentValues): Unit

    /** Method to update the view of elapsed simulation time.
      * @param timerValue
      *   current value of the simulation timer
      */
    def displayElapsedTime(timerValue: String): Unit

    def displayGreenHouseDivisionView(ghDivisionView: GHDivisionView): Unit

    /** Method to notify view that the simulation time has finished and to display [[FinishSimulationView]]. */
    def finishSimulation(): Unit
    
    def moveToNextScene(finishSimulationView: FinishSimulationView) : Unit

  /** Trait that represents the provider of the view for environment values and simulation time visualization. */
  trait Provider:
    val environmentView: EnvironmentView

  type Requirements = EnvironmentControllerModule.Provider

  /** Trait that represents the components of the view for environment values and simulation time visualization. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[EnvironmentView]] implementation.
      * @param simulationView
      *   the root view of the application.
      * @param baseView
      *   the view in which the [[EnvironmentView]] is enclosed.
      */
    class EnvironmentViewImpl(simulationView: SimulationView, private val baseView: BaseView)
        extends AbstractViewComponent[BorderPane]("environment.fxml")
        with EnvironmentView:

      override val component: BorderPane = loader.load[BorderPane]

      @FXML
      var timeSpeedSlider: Slider = _

      @FXML
      var setDayLabel: Label = _

      @FXML
      var setLocationLabel: Label = _

      @FXML
      var setTemperatureLabel: Label = _

      @FXML
      var setHumidityLabel: Label = _

      @FXML
      var setUvIndexLabel: Label = _

      @FXML
      var setLuxLabel: Label = _

      @FXML
      var setConditionLabel: Label = _

      @FXML
      var timeElapsedLabel: Label = _

      timeSpeedSlider.setOnMouseReleased(_ => notifySpeedChange(timeSpeedSlider.getValue))

      baseView.changeSceneButton.setText("Stop simulation")
      baseView.changeSceneButton.setOnMouseClicked { _ =>
        context.environmentController.stopSimulation()
        context.environmentController.nextMVC(baseView)
      }

      override def displayNameCity(cityName: String): Unit =
        Platform.runLater(() => setLocationLabel.setText(cityName))

      override def displayEnvironmentValues(environmentValues: EnvironmentValues): Unit =
        Platform.runLater(() =>
          val date: String = environmentValues("time").toString.split(" ")(0)
          setDayLabel.setText(date)
          val temperature: String = environmentValues("temp_c").toString
          setTemperatureLabel.setText(temperature + "°")
          val humidity: String = environmentValues("humidity").toString
          setHumidityLabel.setText(humidity + "%")
          val uvIndex: String = environmentValues("uv").toString
          setUvIndexLabel.setText(uvIndex)
          val lux: String = environmentValues("lux").toString
          setLuxLabel.setText(lux)
          val condition: String = environmentValues("condition").toString
          setConditionLabel.setText(condition)
        )

      override def displayElapsedTime(timerValue: String): Unit =
        Platform.runLater(() => timeElapsedLabel.setText(timerValue))

      override def displayGreenHouseDivisionView(ghDivisionView: GHDivisionView): Unit =
        component.setCenter(ghDivisionView)

      override def finishSimulation(): Unit =
        context.environmentController.nextMVC(baseView)

      override def moveToNextScene(finishSimulationView: FinishSimulationView): Unit =
        Platform.runLater(() => simulationView.changeView(finishSimulationView))

      private def notifySpeedChange(value: Double): Unit =
        context.environmentController.updateVelocityTimer(value)

  /** Trait that encloses the controller for environment values and simulation time visualization. */
  trait Interface extends Provider with Component:
    self: Requirements =>
