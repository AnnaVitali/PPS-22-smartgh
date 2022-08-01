package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.scene.control.*
import javafx.scene.layout.BorderPane

import scala.concurrent.duration.*
import scala.concurrent.duration.Duration.Inf.toSeconds
import scala.language.postfixOps
import java.text.DateFormat
import java.text.SimpleDateFormat

trait GreenHouseGlobalView extends ViewComponent[BorderPane]:

  val greenHouseDivisionView: GreenHouseDivisionView
  type EnvironmentValues = Map[String, Any]
  def setEnvironmentValues(environmentValues: EnvironmentValues) : Unit
  def setTimer(timerValue : FiniteDuration) : Unit

object GreenHouseGlobalView:

  def apply(): GreenHouseGlobalView = GreenHouseGlobalViewImpl()

  private class GreenHouseGlobalViewImpl() extends AbstractViewComponent[BorderPane]("ghGlobal.fxml") with GreenHouseGlobalView:

    override val component: BorderPane = loader.load[BorderPane]

    override val greenHouseDivisionView: GreenHouseDivisionView = GreenHouseDivisionView()
    component.setCenter(greenHouseDivisionView)

    val timeFormat: DateFormat = new SimpleDateFormat("HH:mm:ss")
    val timeElapsedLabel : Label = component.lookup("#timeElapsedLabel").asInstanceOf[Label]
    val defaultTimerValue : FiniteDuration = 0.hours + 0.minutes + 0.seconds
    setTimer(defaultTimerValue)

    val speedSlider: Slider = component.lookup("#timeSpeedSlider").asInstanceOf[Slider]
    speedSlider.setOnMouseReleased(_ => notifySpeedChange(speedSlider.getValue))

    override def setEnvironmentValues(environmentValues: EnvironmentValues) : Unit = ???

    override def setTimer(timerValue : FiniteDuration) : Unit =
      System.out.println(timerValue.toSeconds)
      val timerValueFormatted : String = timeFormat.format(timerValue.toSeconds)
      timeElapsedLabel.setText(timerValueFormatted)

    private def notifySpeedChange(value : Double) : Unit =
      // todo: richiamare metodo changeSpeed di TimeModel
      ???

