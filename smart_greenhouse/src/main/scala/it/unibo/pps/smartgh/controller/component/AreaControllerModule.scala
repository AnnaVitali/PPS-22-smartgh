package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.AreaViewModule

/** Implementation of the [[AreaControllerModule]]. */
object AreaControllerModule:
  /** A trait that represents the controller for the area division. */
  trait AreaController:
    /** Create the area view. */
    def paintArea(): Unit

    /** TODO */
    def openArea(): Unit

  /** A trait for defining the area instance. */
  trait Provider:
    /** The controller of the area. */
    val areaController: AreaController

  /** The controller requirements. */
  type Requirements = AreaViewModule.Provider with AreaModelModule.Provider

  /** A trait that represents the greenhouse area component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the area controller. */
    class AreaControllerImpl extends AreaController:
      override def paintArea(): Unit =
        val color = areaModel.status match
          case AreaModelModule.NORMAL => "#33cc33"
          case _ => "#cc3333"

        areaView.paintArea(areaModel.plant.name, color, areaModel.sensorValues())

      override def openArea(): Unit =
        //TODO
        println(s"open area $areaModel")

  /** Trait that combine provider and component for area controller. */
  trait Interface extends Provider with Component:
    self: Requirements =>