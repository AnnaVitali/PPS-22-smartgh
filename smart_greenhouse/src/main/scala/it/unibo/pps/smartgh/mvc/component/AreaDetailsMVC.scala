package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.AreaDetailsControllerModule
import it.unibo.pps.smartgh.controller.component.AreaDetailsControllerModule.AreaDetailsController
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaModel
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.view.component.AreaDetailsViewModule.AreaDetailsView
import it.unibo.pps.smartgh.view.component.AreaDetailsViewModule

/** A trait that represents the MVC component for the area detail. */
trait AreaDetailsMVC
    extends AreaModelModule.Interface
    with AreaDetailsViewModule.Interface
    with AreaDetailsControllerModule.Interface
    with SimulationMVC.Interface

/** Object that incapsulate the model view and controller module for the area details. */
object AreaDetailsMVC:

  /** Create a new [[AreaDetailsMVCImpl]].
    * @param simulationMVC
    *   the [[SimulationMVC]] of the application.
    * @param areaModel
    *   the model of area.
    * @return
    *   a new instance of [[AreaDetailsMVCImpl]].
    */
  def apply(simulationMVC: SimulationMVC, areaModel: AreaModel): AreaDetailsMVC =
    AreaDetailsMVCImpl(simulationMVC, areaModel)

  private class AreaDetailsMVCImpl(override val simulationMVC: SimulationMVC, override val areaModel: AreaModel)
      extends AreaDetailsMVC:

    override val areaDetailsView: AreaDetailsView = AreaDetailsViewImpl()
    override val areaDetailsController: AreaDetailsController = AreaDetailsControllerImpl()

    areaDetailsController.initializeView()
