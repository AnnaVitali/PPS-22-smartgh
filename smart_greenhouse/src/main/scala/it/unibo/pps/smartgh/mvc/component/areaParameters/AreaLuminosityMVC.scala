package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaLuminosityControllerModule
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaLuminosityControllerModule.AreaLuminosityController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.view.component.areaParameters.AreaLuminosityViewModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaLuminosityViewModule.AreaLuminosityView

object AreaLuminosityMVC:

  def apply(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): AreaLuminosityMVCImpl =
    AreaLuminosityMVCImpl(areaModel, updateStateMessage)

  class AreaLuminosityMVCImpl(model: AreaModel, updateStateMessage: (String, Boolean) => Unit)
      extends AreaModelModule.Interface
      with AreaLuminosityViewModule.Interface
      with AreaLuminosityControllerModule.Interface:

    override val areaModel: AreaModel = model
    override val areaLuminosityController: AreaLuminosityController = AreaLuminosityControllerImpl(updateStateMessage)
    override val areaLuminosityView: AreaLuminosityView = AreaLuminosityViewImpl()

    areaLuminosityController.initializeView(areaLuminosityView)
