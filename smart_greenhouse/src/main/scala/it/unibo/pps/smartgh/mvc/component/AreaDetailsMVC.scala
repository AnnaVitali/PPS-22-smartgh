package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.AreaDetailsControllerModule.AreaDetailsController
import it.unibo.pps.smartgh.controller.component.{AreaControllerModule, AreaDetailsControllerModule}
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.AreaDetailsViewModule.AreaDetailsView
import it.unibo.pps.smartgh.view.component.{AreaDetailsViewModule, BaseView}

object AreaDetailsMVC:

  def apply(simulationMVC: SimulationMVCImpl, baseView: BaseView, areaModel: AreaModel): AreaDetailsMVCImpl =
    AreaDetailsMVCImpl(simulationMVC, baseView, areaModel)

  class AreaDetailsMVCImpl(simulationMVC: SimulationMVCImpl, baseView: BaseView, model: AreaModel)
      extends AreaModelModule.Interface
      with AreaDetailsViewModule.Interface
      with AreaDetailsControllerModule.Interface:

    override val areaModel: AreaModel = areaModel
    override val areaDetailsController: AreaDetailsController = AreaDetailsControllerImpl()
    override val areaDetailsViewModule: AreaDetailsView = AreaDetailsViewImpl(simulationMVC.simulationView, baseView)
