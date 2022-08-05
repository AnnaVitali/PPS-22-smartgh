package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.view.component.EnvironmentViewModule
import it.unibo.pps.smartgh.model.EnvironmentModelModule
import it.unibo.pps.smartgh.controller.TimeController
import monix.execution.Ack.Continue
import org.apache.commons.lang3.time.DurationFormatUtils

import scala.concurrent.duration.FiniteDuration
import monix.execution.Ack.{Continue, Stop}
import monix.execution.Scheduler.Implicits.global
import org.apache.commons.lang3.time.DurationFormatUtils

import scala.language.postfixOps

object EnvironmentControllerModule:

  trait EnvironmentController:

    val timeController : TimeController

    def startSimulation() : Unit

    def stopSimulation() : Unit

    def updateView() : Unit

  trait Provider:
    val controller : EnvironmentController

  type Requirements = EnvironmentViewModule.Provider with EnvironmentModelModule.Provider

  trait Component:
    context: Requirements =>
    class EnvironmentControllerImpl extends EnvironmentController:

      override val timeController: TimeController = TimeController(context.model.time)

      override def startSimulation(): Unit = timeController.startSimulationTimer()

      override def stopSimulation(): Unit = timeController.stopSimulationTimer()

      override def updateView(): Unit =
        context.view.displayNameCity(model.city.name)
        updateTimeValue()
        updateEnvironmentValues()
      
      private def updateTimeValue(): Unit =
        model.time
          .getTimeValueObservable()
          .subscribe(
            (t : FiniteDuration) => {
              if isSimulationEnded(t) then
                stopSimulation()
                view.finishSimulation()
                Stop
              else
                val time : String = DurationFormatUtils.formatDuration(t.toMillis, "HH:mm:ss", true)
                view.displayElapsedTime(time)
                Continue
            },
            (ex: Throwable) => ex.printStackTrace(),
            () => {}
          )

      private def updateEnvironmentValues() : Unit =
        var lastRequestTime : Long = -1
        model.time
          .getTimeValueObservable()
          .subscribe(
            (t : FiniteDuration) => {
              if isSimulationEnded(t) then
                stopSimulation()
                view.finishSimulation()
                Stop
              else
                if t.toHours.>(lastRequestTime) then
                  view.displayEnvironmentValues(model.city.mainEnvironmentValues(t.toHours.intValue))
                  lastRequestTime = t.toHours
                Continue
            },
            (ex: Throwable) => ex.printStackTrace(),
            () => {}
          )

      private def isSimulationEnded(t: FiniteDuration): Boolean = t.toDays.>=(1)

  trait Interface extends Provider with Component:
    self: Requirements =>
