package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.AreaLuminosityControllerModule
import it.unibo.pps.smartgh.controller.component.AreaLuminosityControllerModule.AreaLuminosityController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.AreaLuminosityViewModule.AreaLuminosityView
import it.unibo.pps.smartgh.view.component.{AreaLuminosityViewModule, BaseView}

object AreaLuminosityMVC:

  def apply(simulationMVC: SimulationMVCImpl, baseView: BaseView, areaModel: AreaModel): AreaLuminosityMVCImpl =
    AreaLuminosityMVCImpl(simulationMVC, baseView, areaModel)

  class AreaLuminosityMVCImpl(simulationMVCImpl: SimulationMVCImpl, baseView: BaseView, model: AreaModel)
      extends AreaModelModule.Interface
      with AreaLuminosityViewModule.Interface
      with AreaLuminosityControllerModule.Interface:

    override val areaModel: AreaModel = model
    override val areaLuminosityController: AreaLuminosityController = AreaLuminosityControllerImpl()
    override val areaLuminosityView: AreaLuminosityView = AreaLuminosityViewImpl()
