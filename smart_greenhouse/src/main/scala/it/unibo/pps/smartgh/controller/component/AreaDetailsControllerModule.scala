package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.areaParameter.*
import it.unibo.pps.smartgh.view.component.AreaDetailsViewModule
import monix.execution.Ack.Continue
import monix.execution.Scheduler.Implicits.global

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
  type Requirements = AreaDetailsViewModule.Provider with AreaModelModule.Provider with SimulationMVC.Provider

  /** Trait that represent the controller component for the area details. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaDetailsController]] implementation. */
    class AreaDetailsControllerImpl() extends AreaDetailsController:

      private var messages: Seq[String] = Seq.empty
      private val parametersMVC: Seq[AreaParameterMVC] =
        areaModel.areaSensorHelper.parametersMVC(areaModel, updateStateMessage)

      override def beforeNextScene(): Unit =
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
        simulationMVC.simulationController.subscribeTimerValue(areaDetailsView.updateTime)

      private def updateStateMessage(message: String, show: Boolean): Unit =
        if show && (!messages.contains(message)) then messages = messages prepended message
        else if !show && messages.contains(message) then messages = messages diff Seq(message)
        areaDetailsView.updateStateMessages(messages mkString ("", "\n", ""))

  /** Trait that combine provider and component for area details. */
  trait Interface extends Provider with Component:
    self: Requirements =>
