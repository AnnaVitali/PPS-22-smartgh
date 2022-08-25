package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.AreaDetailsControllerModule
import it.unibo.pps.smartgh.controller.component.AreaDetailsControllerModule.AreaDetailsController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.AreaDetailsViewModule.AreaDetailsView
import it.unibo.pps.smartgh.view.component.AreaDetailsViewModule

/** Object that incapsulate the model view and controller module for the area details. */
object AreaDetailsMVC:

  /** Create a new [[AreaDetailsMVCImpl]].
    * @param simulationMVC
    *   the [[SimulationMVCImpl]] of the application.
    * @param areaModel
    *   the model of area.
    * @return
    *   a new instance of [[AreaDetailsMVCImpl]].
    */
  def apply(simulationMVC: SimulationMVCImpl, areaModel: AreaModel): AreaDetailsMVCImpl =
    AreaDetailsMVCImpl(simulationMVC, areaModel)

  /** Implementation of the area details MVC.
    * @param simulationMVC
    *   the [[SimulationMVCImpl]] of the application.
    * @param areaModel
    *   the model of area.
    */
  class AreaDetailsMVCImpl(override val simulationMVC: SimulationMVCImpl, override val areaModel: AreaModel)
      extends AreaModelModule.Interface
      with AreaDetailsViewModule.Interface
      with AreaDetailsControllerModule.Interface
      with SimulationMVC.Interface:

    override val areaDetailsView: AreaDetailsView = AreaDetailsViewImpl()
    override val areaDetailsController: AreaDetailsController = AreaDetailsControllerImpl()

    areaDetailsController.initializeView()
