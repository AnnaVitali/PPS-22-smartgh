package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.TimeController
import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.application.Platform
import scala.concurrent.duration.*

trait GreenHouseGlobalView extends ViewComponent[BorderPane]:

  val greenHouseDivisionView: GreenHouseDivisionView
  type EnvironmentValues = Map[String, Any]
  def setEnvironmentValues(environmentValues: EnvironmentValues): Unit
  def setTimer(timerValue: String): Unit

object GreenHouseGlobalView:

  def apply(simulationView: SimulationView, baseView: BaseView): GreenHouseGlobalView =
    GreenHouseGlobalViewImpl(simulationView, baseView)

  private class GreenHouseGlobalViewImpl(private val simulationView: SimulationView, baseView: BaseView)
      extends AbstractViewComponent[BorderPane]("ghGlobal.fxml")
      with GreenHouseGlobalView:

    override val component: BorderPane = loader.load[BorderPane]

    override val greenHouseDivisionView: GreenHouseDivisionView = GreenHouseDivisionView()
    component.setCenter(greenHouseDivisionView)

    val controller: TimeController = TimeController()

    val timeElapsedLabel: Label = component.lookup("#timeElapsedLabel").asInstanceOf[Label]

    val speedSlider: Slider = component.lookup("#timeSpeedSlider").asInstanceOf[Slider]

    controller.view = this
    controller.startSimulationTimer()
    speedSlider.setOnMouseReleased(_ => notifySpeedChange(speedSlider.getValue))
    baseView.changeSceneButton.setText("Stop simulation")
    baseView.changeSceneButton.setOnMouseClicked { _ =>
      //todo
      simulationView.changeView(FinishSimulationView(simulationView, baseView))
    }

    override def setEnvironmentValues(environmentValues: EnvironmentValues): Unit = ???

    override def setTimer(timerValue: String): Unit =
      Platform.runLater(() => timeElapsedLabel.setText(timerValue))

    private def notifySpeedChange(value: Double): Unit =
      controller.updateVelocityTimer(value)
