package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.controller.EnvironmentControllerModule.EnvironmentController
import it.unibo.pps.smartgh.model.EnvironmentModelModule.EnvironmentModel
import it.unibo.pps.smartgh.model.time.TimeModel
import it.unibo.pps.smartgh.view.component.EnvironmentViewModule.EnvironmentView

import scala.concurrent.duration.*
import scala.language.postfixOps
import scala.math.{exp, log, pow}
import org.apache.commons.lang3.time.DurationFormatUtils

/** A trait that represents the controller for the simulation timer. */
trait TimeController:

//  /** Getter method for the view component.
//    * @return
//    *   the view assoociated to the controller.
//    */
//  def view: EnvironmentViewModule.Interface
//
//  /** Setter method for the view component.
//    * @param view
//    *   the view associated to the controller.
//    */
//  def view_=(view: EnvironmentViewModule.Interface): Unit

  /** Method that notify the controller to start the simulation timer. */
  def startSimulationTimer(): Unit

  /** Method that notify the controller to stop the simulation timer. */
  def stopSimulationTimer(): Unit

  /** Method that notify the controller to update the speed of the simulation timer.
    * @param speed
    *   time that has to pass before emitting new items.
    */
  def updateVelocityTimer(speed: Double): Unit

//  /** Method that requires the controller to update the time shown on the view.
//    * @param time
//    */
//  def updateTimeValueOnView(): Unit

//  /** Method that is called when the simulation is finished. */
//  def finishSimulation(): Unit

  def notifyTimeValueChange(timeValue: FiniteDuration) : Unit

/** Object that can be used to create a new instance of [[TimeController]]. */
object TimeController:

  /** Create a new [[TimeController]].
    * @return
    *   a new instance of [[TimeController]].
    */
  def apply(timeModel : TimeModel, environmentView: EnvironmentView, environmentController : EnvironmentController): TimeController = TimeControllerImpl(timeModel, environmentView, environmentController)

  private class TimeControllerImpl(timeModel : TimeModel, environmentView: EnvironmentView, environmentController : EnvironmentController) extends TimeController:

    // private val model = TimeModel()
    // override val view: EnvironmentViewModule.Interface = _

    var lastRequestTime : Long = -1
    
    private val timeSpeed: Double => FiniteDuration = (x: Double) =>
      val x0 = 1
      val x1 = 10
      val y0 = pow(10, 6)
      val y1 = 50
      exp(((x - x0) / (x1 - x0)) * (log(y1) - log(y0)) + log(y0)) microseconds

    timeModel.controller = this

    override def startSimulationTimer(): Unit = timeModel.start()

    override def stopSimulationTimer(): Unit = timeModel.stop()

    override def updateVelocityTimer(speed: Double): Unit = timeModel.setSpeed(timeSpeed(speed))

    override def notifyTimeValueChange(timeValue: FiniteDuration): Unit =
      if isSimulationEnded(timeValue) then
          stopSimulationTimer()
          environmentView.finishSimulation()
      else
        val time : String = DurationFormatUtils.formatDuration(timeValue.toMillis, "HH:mm:ss", true)
        environmentView.displayElapsedTime(time)
        hasNewHourPassed(timeValue)
    
    private def isSimulationEnded(timeValue: FiniteDuration): Boolean = timeValue.toDays.>=(1)

    private def hasNewHourPassed(timeValue: FiniteDuration): Unit = 
      if timeValue.toHours.>(lastRequestTime) then
        environmentController.notifyEnvironmentValuesChange(timeValue.toHours.intValue)
      else
        lastRequestTime = timeValue.toHours

//    override def finishSimulation(): Unit = ???
//      // view.finishSimulation()
