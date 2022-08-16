package it.unibo.pps.smartgh.mvc.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaLuminosityControllerModule.AreaLuminosityController
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaLuminosityControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.BaseView
import it.unibo.pps.smartgh.view.component.areaParameters.AreaLuminosityViewModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaLuminosityViewModule.AreaLuminosityView

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
