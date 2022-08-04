package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.view.component.EnvironmentViewModule
import it.unibo.pps.smartgh.model.EnvironmentModelModule

object EnvironmentControllerModule:

  trait EnvironmentController:

    val timeController : TimeController

    /** Update environment's view */
    def updateView(): Unit

  trait Provider:
    val controller : EnvironmentController

  type Requirements = EnvironmentViewModule.Provider with EnvironmentModelModule.Provider

  trait Component:
    context: Requirements =>
    class EnvironmentControllerImpl extends EnvironmentController:

      override val timeController: TimeController = TimeController(model.time, view)

      override def updateView(): Unit = ???

  trait Interface extends Provider with Component:
    self: Requirements =>
