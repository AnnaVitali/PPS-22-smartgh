package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.mvc.AreaMVC.AreaMVCImpl
import it.unibo.pps.smartgh.view.component.GHViewModule
import monix.reactive.Observable
import monix.execution.Scheduler.Implicits.global
import concurrent.duration.DurationInt
import scala.language.postfixOps

/** Implementation of the [[GHControllerModule]]. */
object GHControllerModule:
  /** A trait that represents the controller for the greenhouse division. */
  trait GreenHouseController:
    /** Update division view */
    def updateView(): Unit

  /** A trait for defining the controller instance.*/
  trait Provider:
    val ghController: GreenHouseController

  type Requirements = GHViewModule.Provider with GHModelModule.Provider
  /** A trait that represents the greenhouse controller component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the greenhouse controller.*/
    class GreenHouseDivisionControllerImpl() extends GreenHouseController:
      val timeoutUpd = Observable.interval(5.seconds)
        .map(_ =>
          ghDivisionModel.areas.foreach(a => a.paintArea())
          context.ghDivisionView.paintDivision(ghDivisionModel.dimension._1, ghDivisionModel.dimension._2, ghDivisionModel.areas.map(a => a.areaView))
        ). take(10)

      override def updateView(): Unit =
        timeoutUpd.subscribe()

  /** Trait that combine provider and component for greenhouse controller.*/
  trait Interface extends Provider with Component:
    self: Requirements =>