package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.mvc.AreaMVC.AreaMVCImpl
import it.unibo.pps.smartgh.view.component.GHViewModule

/** Implementation of the [[GHControllerModule]]. */
object GHControllerModule:
  /** A trait that represents the controller for the greenhouse division. */
  trait Controller:
    /** Update division view */
    def updateView(): Unit

  /** A trait for defining the controller instance.*/
  trait Provider:
    val ghController: Controller

  type Requirements = GHViewModule.Provider with GHModelModule.Provider
  /** A trait that represents the greenhouse controller component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the greenhouse controller.*/
    class GreenHouseDivisionControllerImpl() extends Controller:
      //TODO create List of MVC areas, then pass to the view the area view that will be added to the main division

      def updateView(): Unit =
        ghDivisionModel.areas.foreach(a => a.paintArea())
        context.ghDivisionView.paintDivision(ghDivisionModel.dimension._1, ghDivisionModel.dimension._2, ghDivisionModel.areas.map(a => a.areaView))

  /** Trait that combine provider and component for greenhouse controller.*/
  trait Interface extends Provider with Component:
    self: Requirements =>