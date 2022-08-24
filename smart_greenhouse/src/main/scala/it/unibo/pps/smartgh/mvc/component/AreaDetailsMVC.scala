package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.AreaDetailsControllerModule
import it.unibo.pps.smartgh.controller.component.AreaDetailsControllerModule.AreaDetailsController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.AreaDetailsViewModule.AreaDetailsView
import it.unibo.pps.smartgh.view.component.{AreaDetailsViewModule, BaseView}

/** Object that incapsulate the model view and controller module for the area details. */
object AreaDetailsMVC:

  /** Create a new [[AreaDetailsMVCImpl]].
    * @param simulationMVC
    *   the [[SimulationMVCImpl]] of the application.
    * @param baseView
    *   the view in which the [[AreaDetailsMVCImpl]] is enclosed.
    * @param areaModel
    *   the model of area.
    * @return
    *   a new instance of [[AreaDetailsMVCImpl]].
    */
  def apply(simulationMVC: SimulationMVCImpl, baseView: BaseView, areaModel: AreaModel): AreaDetailsMVCImpl =
    AreaDetailsMVCImpl(simulationMVC, baseView, areaModel)

  /** Implementation of the area details MVC.
    * @param simulationMVC
    *   the [[SimulationMVCImpl]] of the application.
    * @param baseView
    *   the view in which the [[AreaDetailsMVCImpl]] is enclosed.
    * @param model
    *   the model of area.
    */
  class AreaDetailsMVCImpl(simulationMVC: SimulationMVCImpl, baseView: BaseView, model: AreaModel)
      extends AreaModelModule.Interface
      with AreaDetailsViewModule.Interface
      with AreaDetailsControllerModule.Interface:

    override val areaModel: AreaModel = model
    override val areaDetailsView: AreaDetailsView = AreaDetailsViewImpl(simulationMVC.simulationView, baseView)
    override val areaDetailsController: AreaDetailsController = AreaDetailsControllerImpl(simulationMVC)

    areaDetailsController.initializeView()
