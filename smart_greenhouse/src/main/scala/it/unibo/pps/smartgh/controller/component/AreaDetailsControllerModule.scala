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

/** Object that encloses the controller module for the area details. */
object AreaDetailsControllerModule:

  /** A trait that represents the area details controller. */
  trait AreaDetailsController extends SceneController:

    /** Initialize the view scene. */
    def initializeView(): Unit

  /** Trait that represents the provider of the controller for the area details. */
  trait Provider:

    /** The controller of area details. */
    val areaDetailsController: AreaDetailsController

  /** The controller requirements. */
  type Requirements = AreaDetailsViewModule.Provider with AreaModelModule.Provider

  /** Trait that represent the controller component for the area details. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaDetailsController]] implementation.
      * @param simulationMVC
      *   the simulationMVC of the application.
      */
    class AreaDetailsControllerImpl(simulationMVC: SimulationMVCImpl) extends AreaDetailsController:

      private var messages: Seq[String] = Seq.empty
      private val parametersMVC: Seq[AreaParametersMVC] =
        areaModel.areaSensorHelper.parametersMVC(areaModel, updateStateMessage)

      override def instantiateNextSceneMVC(baseView: BaseView): Unit =
        areaDetailsView.moveToNextScene(simulationMVC.simulationController.environmentController.envView())
        parametersMVC.foreach(_.parameterController.stopListening())
        simulationMVC.simulationController.environmentController.backToEnvironment()

      override def initializeView(): Unit =
        areaModel
          .changeStatusObservable()
          .subscribe(
            { s =>
              areaDetailsView.updateState(s.toString); Continue
            },
            _.printStackTrace(),
            () => {}
          )
        areaDetailsView.updateState(areaModel.status.toString)
        areaDetailsView.initializeParameters(parametersMVC.map(_.parameterView))
        val plant = areaModel.plant
        areaDetailsView.updatePlantInformation(plant.name, plant.description, plant.imageUrl)
        simulationMVC.simulationController.environmentController.subscribeTimerValue(areaDetailsView.updateTime)

      private def updateStateMessage(message: String, show: Boolean): Unit =
        if show && (!messages.contains(message)) then messages = messages.prepended(message)
        else if !show && messages.contains(message) then messages = messages.filter(!_.contentEquals(message))
        areaDetailsView.updateStateMessages(messages.mkString("", "\n", ""))

  /** Trait that combine provider and component for area details. */
  trait Interface extends Provider with Component:
    self: Requirements =>
