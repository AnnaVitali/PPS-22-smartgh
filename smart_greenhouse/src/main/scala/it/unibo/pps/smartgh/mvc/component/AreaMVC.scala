package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.AreaControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.time.Timer
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.GreenHouseDivisionMVC
import it.unibo.pps.smartgh.view.component.AreaViewModule

/** A trait that represents the MVC component for an area. */
trait AreaMVC
    extends AreaModelModule.Interface
    with AreaViewModule.Interface
    with AreaControllerModule.Interface
    with SimulationMVC.Interface:

  /** Paint the area. */
  def paintArea(): Unit

/** Object that can be used to create a new instance of [[AreaMVCImpl]]. */
object AreaMVC:
  /** Create a new [[AreaMVC]].
    * @param plant
    *   of the Area
    * @param simulationMVC
    *   the simulation MVC instance
    * @param greenHouseDivisionMVC
    *   instance of the [[GreenHouseDivisionMVC]]
    * @return
    *   a new instance of [[AreaMVC]].
    */
  def apply(
      plant: Plant,
      simulationMVC: SimulationMVC,
      greenHouseDivisionMVC: GreenHouseDivisionMVC
  ): AreaMVC =
    AreaMVCImpl(plant, simulationMVC, greenHouseDivisionMVC)

  private class AreaMVCImpl(
      plant: Plant,
      override val simulationMVC: SimulationMVC,
      greenHouseDivisionMVC: GreenHouseDivisionMVC
  ) extends AreaMVC:

    override val areaModel: AreaModelModule.AreaModel =
      AreaImpl(plant, simulationMVC.simulationController.subscribeTimerValue)
    override val areaView: AreaViewModule.AreaView = AreaViewImpl()
    override val areaController: AreaControllerModule.AreaController = AreaControllerImpl(greenHouseDivisionMVC)

    override def paintArea(): Unit = areaController.paintArea()
