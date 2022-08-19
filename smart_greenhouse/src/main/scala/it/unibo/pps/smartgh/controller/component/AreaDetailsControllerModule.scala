package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.{AreaDetailsViewModule, BaseView}
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaStatus
import monix.execution.Ack.Continue
import monix.execution.Scheduler.Implicits.global
import org.apache.commons.lang3.time.DurationFormatUtils

import scala.concurrent.duration.FiniteDuration

object AreaDetailsControllerModule:

  trait AreaDetailsController extends SceneController:
    def initializeView(): Unit

  trait Provider:
    val areaDetailsController: AreaDetailsController

  type Requirements = AreaDetailsViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaDetailsControllerImpl(simulationMVC: SimulationMVCImpl) extends AreaDetailsController:

      private var messages: Seq[String] = Seq.empty

      override def instantiateNextSceneMVC(baseView: BaseView): Unit = ???

      override def initializeView(): Unit =
        areaModel
          .changeStatusObservable()
          .subscribe(
            (s: AreaStatus) => {
              s match
                case AreaStatus.ALARM => areaDetailsView.updateState(s.toString)
                case _ =>
              Continue
            },
            (ex: Throwable) => ex.printStackTrace(),
            () => {}
          )
        areaDetailsView.initializeParameters(simulationMVC, areaModel, updateStateMessage)
        val plant = areaModel.plant
        areaDetailsView.updatePlantInformation(plant.name, plant.description, plant.imageUrl)
        areaModel.timer.addCallback(
          time => areaDetailsView.updateTime(DurationFormatUtils.formatDuration(time.toMillis, "HH:mm:ss", true)),
          1
        )

      private def updateStateMessage(message: String, show: Boolean): Unit =
        if show && (!messages.contains(message)) then messages = messages.prepended(message)
        else if !show && messages.contains(message) then messages = messages.filter(m => !m.contentEquals(message))
        areaDetailsView.updateStateMessages(messages.mkString)

  trait Interface extends Provider with Component:
    self: Requirements =>
