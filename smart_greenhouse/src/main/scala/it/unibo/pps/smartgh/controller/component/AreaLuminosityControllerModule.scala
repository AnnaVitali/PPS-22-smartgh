package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.{AreaLuminosityViewModule, BaseView}

object AreaLuminosityControllerModule:

  trait AreaLuminosityController extends SceneController

  trait Provider:
    val areaLuminosityController: AreaLuminosityController

  type Requirements = AreaLuminosityViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaLuminosityControllerImpl() extends AreaLuminosityController:

      override def instantiateNextSceneMVC(baseView: BaseView): Unit = ???

  trait Interface extends Provider with Component:
    self: Requirements =>
