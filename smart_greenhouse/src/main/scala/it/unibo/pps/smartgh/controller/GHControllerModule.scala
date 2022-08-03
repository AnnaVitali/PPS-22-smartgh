package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.greenhouse.GHModelModule
import it.unibo.pps.smartgh.view.component.GHViewModule

/** Implementation of the [[GreenHouseDivisionController]]. */
object GHControllerModule:
  trait Controller:
    /** Update division view */
    def updateView(): Unit

    /** Open the selected area
     * @return
     *   the selected area
     */
    def openArea(area: (String, Boolean)): (String, Boolean)
  trait Provider:
    val controller: Controller
  type Requirements = GHViewModule.Provider with GHModelModule.Provider
  trait Component:
    context: Requirements =>
    class GreenHouseDivisionControllerImpl extends Controller:
      def updateView(): Unit =
        context.view.paintDivision(model.dimension._1, model.dimension._2, model.areas.map((_, true)))

      def openArea(area: (String, Boolean)): (String, Boolean) =
        println(s"open area $area")
        area

  trait Interface extends Provider with Component:
    self: Requirements =>