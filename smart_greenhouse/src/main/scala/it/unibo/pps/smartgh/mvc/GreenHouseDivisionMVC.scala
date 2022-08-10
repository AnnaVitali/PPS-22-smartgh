package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.GHControllerModule
import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.time.Timer
import it.unibo.pps.smartgh.view.component.GHViewModule
import monix.reactive.subjects.ConcurrentSubject

/** Object that can be used to create a new instance of [[GreenHouseDivisionMVC]]. */
object GreenHouseDivisionMVC:
  /** Create a new [[GreenHouseDivisionMVCImpl]].
    * @param plants
    *   list of [[Plant]]
    * @return
    *   a new instance of [[GreenHouseDivisionMVCImpl]].
    */
  def apply(plants: List[Plant]): GreenHouseDivisionMVCImpl = GreenHouseDivisionMVCImpl(plants)

  /** Create a new [[GreenHouseDivisionMVCImpl]].
    * @param plants
    *   list of [[Plant]]
    * @return
    *   a new instance of [[GreenHouseDivisionMVCImpl]].
    */
  class GreenHouseDivisionMVCImpl(plants: List[Plant])
      extends GHModelModule.Interface
      with GHViewModule.Interface
      with GHControllerModule.Interface:

    override val ghDivisionModel = GreenHouseImpl(plants)
    override val ghController = GreenHouseDivisionControllerImpl()
    override val ghDivisionView = GreenHouseDivisionViewImpl()

    /** Create and set the greenhouse division areas.
      * @param timer
      *   of the simulation
      * @param subjects
      *   for the sensors
      */
    def setAreas(timer: Timer, subjects: Map[String, ConcurrentSubject[Double, Double]]): Unit =
      ghDivisionModel.areas =
        for p <- plants
        yield AreaMVC(p, timer)

      ghDivisionModel.areas.foreach(a => a.areaModel.setSensorSubjects(subjects))

    /** Show the greenhouse and its division. */
    def show(): Unit = ghController.updateView()
