package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.{AreaDetailsViewModule, BaseView}

object AreaDetailsControllerModule:

  trait AreaDetailsController extends SceneController

  trait Provider:
    val areaDetailsController: AreaDetailsController

  type Requirements = AreaDetailsViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaDetailsControllerImpl() extends AreaDetailsController:

      override def instantiateNextSceneMVC(baseView: BaseView): Unit = ???

  trait Interface extends Provider with Component:
    self: Requirements =>
