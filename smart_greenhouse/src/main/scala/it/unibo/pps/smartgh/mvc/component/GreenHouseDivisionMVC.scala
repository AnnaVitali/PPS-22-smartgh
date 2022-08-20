package it.unibo.pps.smartgh.mvc.component

import it.unibo.pps.smartgh.controller.component.GHControllerModule
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.time.Timer
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.component.AreaMVC
import it.unibo.pps.smartgh.view.component.GHViewModule
import monix.reactive.subjects.ConcurrentSubject

/** Object that can be used to create a new instance of [[GreenHouseDivisionMVC]]. */
object GreenHouseDivisionMVC:
  /** Create a new [[GreenHouseDivisionMVCImpl]].
    * @param plants
    *   list of [[Plant]]
    * @param simulationMVC
    *   the simulation MVC instance
    * @return
    *   a new instance of [[GreenHouseDivisionMVCImpl]].
    */
  def apply(plants: List[Plant], simulationMVC: SimulationMVCImpl): GreenHouseDivisionMVCImpl =
    GreenHouseDivisionMVCImpl(plants, simulationMVC)

  /** Create a new [[GreenHouseDivisionMVCImpl]].
    * @param plants
    *   list of [[Plant]]
    * @param simulationMVC
    *   the simulation MVC instance
    * @return
    *   a new instance of [[GreenHouseDivisionMVCImpl]].
    */
  class GreenHouseDivisionMVCImpl(plants: List[Plant], simulationMVC: SimulationMVCImpl)
      extends GHModelModule.Interface
      with GHViewModule.Interface
      with GHControllerModule.Interface:

    override val ghDivisionModel: GHModelModule.GreenHouseModel = GreenHouseImpl(plants)
    override val ghController: GHControllerModule.GreenHouseController = GreenHouseDivisionControllerImpl()
    override val ghDivisionView: GHViewModule.GHDivisionView = GreenHouseDivisionViewImpl()

    /** Create and set the greenhouse division areas.
      * @param timer
      *   of the simulation
      * @param subjects
      *   for the sensors
      */
    def setAreas(timer: Timer, subjects: Map[String, ConcurrentSubject[Double, Double]]): Unit =
      ghDivisionModel.areas =
        for p <- plants
        yield AreaMVC(p, timer, simulationMVC)

      ghDivisionModel.areas.foreach(a => a.areaModel.setSensorSubjects(subjects))

    /** Show the greenhouse and its division. */
    def show(): Unit = ghController.updateView()