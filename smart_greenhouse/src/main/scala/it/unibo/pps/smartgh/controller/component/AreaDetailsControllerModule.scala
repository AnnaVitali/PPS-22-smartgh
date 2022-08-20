package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaStatus
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.component.areaParameters.{
  AreaAirHumidityMVC,
  AreaLuminosityMVC,
  AreaParametersMVC,
  AreaSoilMoistureMVC,
  AreaTemperatureMVC
}
import it.unibo.pps.smartgh.view.component.{AreaDetailsViewModule, BaseView}
import monix.execution.Ack.Continue
import monix.execution.Scheduler.Implicits.global
import org.apache.commons.lang3.time.DurationFormatUtils

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

      private val sensorMVC: Seq[AreaParametersMVC] = Seq(
        AreaLuminosityMVC(areaModel, updateStateMessage),
        AreaTemperatureMVC(areaModel, updateStateMessage),
        AreaAirHumidityMVC(areaModel, updateStateMessage),
        AreaSoilMoistureMVC(areaModel, updateStateMessage)
      )

      override def instantiateNextSceneMVC(baseView: BaseView): Unit =
        areaDetailsView.moveToNextScene(simulationMVC.simulationController.environmentController.envView())
        sensorMVC.foreach(s => s.controller.stopListening())
        simulationMVC.simulationController.environmentController.envView().setBackButton()

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
        areaDetailsView.initializeParameters(sensorMVC.map(p => p.view))
        val plant = areaModel.plant
        areaDetailsView.updatePlantInformation(plant.name, plant.description, plant.imageUrl)
        simulationMVC.simulationController.environmentController.subscribeTimerValue(areaDetailsView.updateTime)

      private def updateStateMessage(message: String, show: Boolean): Unit =
        if show && (!messages.contains(message)) then messages = messages.prepended(message)
        else if !show && messages.contains(message) then messages = messages.filter(m => !m.contentEquals(message))
        areaDetailsView.updateStateMessages(messages.mkString("", "\n", ""))

  trait Interface extends Provider with Component:
    self: Requirements =>
