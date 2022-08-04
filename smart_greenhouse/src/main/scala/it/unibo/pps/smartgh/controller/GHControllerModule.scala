package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.view.component.GHViewModule

/** Implementation of the [[GHControllerModule]]. */
object GHControllerModule:
  /** A trait that represents the controller for the greenhouse division. */
  trait Controller:
    /** Update division view */
    def updateView(): Unit

    /** Open the selected area
     * @return
     *   the selected area
     */
    def openArea(area: (String, Boolean)): (String, Boolean)

  /** A trait for defining the controller instance.*/
  trait Provider:
    val controller: Controller

  type Requirements = GHViewModule.Provider with GHModelModule.Provider
  /** A trait that represents the greenhouse controller component. */
  trait Component:
    context: Requirements =>
    /** Implementation of the greenhouse controller.*/
    class GreenHouseDivisionControllerImpl extends Controller:
      def updateView(): Unit =
        context.view.paintDivision(model.dimension._1, model.dimension._2, model.areas.map((_, true)))

      def openArea(area: (String, Boolean)): (String, Boolean) =
        println(s"open area $area")
        area

  /** Trait that combine provider and component for greenhouse controller.*/
  trait Interface extends Provider with Component:
    self: Requirements =>