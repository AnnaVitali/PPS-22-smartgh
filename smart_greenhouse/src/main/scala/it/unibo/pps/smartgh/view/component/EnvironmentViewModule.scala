package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.EnvironmentControllerModule
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.GHViewModule.GreenHouseView
import it.unibo.pps.smartgh.view.component.HelpView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import scalafx.application.JFXApp3.Stage

import scala.language.postfixOps

/** Object that encloses the view module to display ambient environment's values and simulation time. */
object EnvironmentViewModule:

  /** A trait that represents the environment's view of the application. */
  trait EnvironmentView extends ViewComponent[BorderPane]:

    /** Data structure that will contains environment values. */
    type EnvironmentValues = Map[String, Any]

    /** Method to display the selected city's name in location label.
      * @param cityName
      *   selected city's name
      */
    def displayNameCity(cityName: String): Unit

    /** Method to update the view of environment values.
      * @param environmentValues
      *   current environment values related to the city selected by the user
      */
    def displayEnvironmentValues(environmentValues: EnvironmentValues): Unit

    /** Method to update the view of elapsed simulation time.
      * @param timerValue
      *   current value of the simulation timer
      */
    def displayElapsedTime(timerValue: String): Unit

    /** Method to display [[GreenHouseView]] in the [[EnvironmentView]].
      *
      * @param ghDivisionView
      *   view that represents the green house division in areas
      */
    def displayGreenHouseDivisionView(ghDivisionView: GreenHouseView): Unit

    /** Method to notify view that the simulation time has finished. */
    def finishSimulation(): Unit

    /** Set the back button. */
    def setBackButton(): Unit

  /** Trait that represents the provider of the view for environment values and simulation time visualization. */
  trait Provider:

    /** The view of the environment. */
    val environmentView: EnvironmentView

  /** The view requirements. */
  type Requirements = EnvironmentControllerModule.Provider with SimulationMVC.Provider

  /** Trait that represents the components of the view for environment values and simulation time visualization. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[EnvironmentView]] implementation. */
    class EnvironmentViewImpl() extends AbstractViewComponent[BorderPane]("environment.fxml") with EnvironmentView:

      //noinspection VarCouldBeVal
      @FXML
      protected var timeSpeedSlider: Slider = _

      @FXML
      protected var setDayLabel: Label = _

      //noinspection VarCouldBeVal
      @FXML
      protected var setLocationLabel: Label = _

      @FXML
      protected var setTemperatureLabel: Label = _

      @FXML
      protected var setHumidityLabel: Label = _

      @FXML
      protected var setUvIndexLabel: Label = _

      @FXML
      protected var setLuxLabel: Label = _

      @FXML
      protected var setConditionLabel: Label = _

      //noinspection VarCouldBeVal
      @FXML
      protected var timeElapsedLabel: Label = _

      //noinspection VarCouldBeVal
      @FXML
      protected var helpButton: Button = _

      helpButton.setOnMouseClicked { _ =>
        val helpView = HelpView(new Stage())
        helpButton.setDisable(true)
        helpView.getScene.getWindow.setOnCloseRequest(_ => helpButton.setDisable(false))
        this.component.getScene.getWindow.setOnCloseRequest(_ => helpView.closeWindow())
      }

      timeSpeedSlider.setOnMouseReleased(_ => notifySpeedChange(timeSpeedSlider.getValue))

      setBackButton()

      override def setBackButton(): Unit =
        simulationMVC.simulationView.changeSceneButtonBehaviour(
          "Stop simulation",
          _ => {
            environmentController.stopSimulation()
            finishSimulation()
          }
        )
        simulationMVC.simulationView.changeSceneButtonStyle("alarmButton")

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

      override def displayGreenHouseDivisionView(ghDivisionView: GreenHouseView): Unit =
        component.setCenter(ghDivisionView)

      override def finishSimulation(): Unit =
        Platform.runLater { () =>
          simulationMVC.simulationView.changeSceneButtonStyle("normalButton")
          simulationMVC.simulationView.changeView(FinishSimulationView(simulationMVC))
        }

      private def notifySpeedChange(value: Double): Unit =
        environmentController.updateVelocityTimer(value)

  /** Trait that encloses the view for environment values and simulation time visualization. */
  trait Interface extends Provider with Component:
    self: Requirements =>
