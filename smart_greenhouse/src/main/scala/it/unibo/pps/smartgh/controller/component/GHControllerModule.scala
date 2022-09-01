package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaStatus
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.view.component.GHViewModule
import monix.execution.Ack.Continue
import monix.execution.Cancelable
import monix.execution.Scheduler.Implicits.global
import monix.reactive.Observable

import scala.concurrent.duration.DurationInt

/** Implementation of the [[GHControllerModule]]. */
object GHControllerModule:
  /** A trait that represents the controller for the greenhouse division. */
  trait GreenHouseController:
    /** Update division view. */
    def updateView(): Unit

    /** Cancel the subscription for the view update, so for both the timeout update and the alarm notification. */
    def stopListening(): Unit

  /** A trait for defining the controller instance. */
  trait Provider:
    /** The controller of the Green House. */
    val ghDivisionController: GreenHouseController

  /** The controller requirements. */
  type Requirements = GHViewModule.Provider with GHModelModule.Provider

  /** A trait that represents the greenhouse controller component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the greenhouse controller. */
    class GreenHouseDivisionControllerImpl() extends GreenHouseController:

      private def drawView(): Unit =
        ghDivisionModel.areas.foreach(a => a.paintArea())
        ghDivisionView.paintDivision(ghDivisionModel.areas.map(a => a.areaView))

      private val timeoutUpd = Observable
        .interval(3.seconds)
        .map(_ => drawView())
      private var subscriptionTimeout: Cancelable = _
      private val subscriptionAlarm: List[Cancelable] = List.empty

      override def updateView(): Unit =
        ghDivisionModel.areas.foreach(
          _.areaModel
            .changeStatusObservable()
            .subscribe(
              s => {
                s match
                  case AreaStatus.ALARM => drawView()
                  case _ =>
                Continue
              },
              _.printStackTrace(),
              () => {}
            ) :: subscriptionAlarm
        )
        subscriptionTimeout = timeoutUpd.subscribe()

      override def stopListening(): Unit =
        subscriptionTimeout.cancel()
        subscriptionAlarm.foreach(a => a.cancel())

  /** Trait that combine provider and component for greenhouse controller. */
  trait Interface extends Provider with Component:
    self: Requirements =>
