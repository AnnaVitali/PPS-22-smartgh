package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.AreaViewModule

/** Implementation of the [[AreaControllerModule]]. */
object AreaControllerModule:
  /** A trait that represents the controller for the area division. */
  trait Controller:
    /** TODO*/
    def paintArea():Unit

    /** TODO*/
    def openArea():Unit

  /** A trait for defining the area instance.*/
  trait Provider:
    val areaController: Controller

  type Requirements = AreaViewModule.Provider with AreaModelModule.Provider
  /** A trait that represents the greenhouse area component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the area controller.*/
    class AreaControllerImpl extends Controller:
      override def paintArea(): Unit =

        val color = if areaModel.status == AreaModelModule.NORMAL then "#33cc33" else "#cc3333"
        areaView.paintArea(areaModel.plant.name, color)

      override def openArea(): Unit =
        println(s"open area $areaModel")


  /** Trait that combine provider and component for area controller.*/
  trait Interface extends Provider with Component:
    self: Requirements =>
