package it.unibo.pps.smartgh.controller

import it.unibo.pps.smartgh.model.greenhouse.GreenHouse
import it.unibo.pps.smartgh.view.component.GreenHouseDivisionView

trait GreenHouseDivisionController:
  /** Update division view */
  def updateView(): Unit

  /** Open the selected area
    * @return
    *   the selected area
    */
  def openArea(area: (String, Boolean)): (String, Boolean)

  /** @return the view instance */
  var view: GreenHouseDivisionView

/** Implementation of the [[GreenHouseDivisionController]]. */
object GreenHouseDivisionController:

  /** Apply method for the [[GreenHouseDivisionController]].
    * @return
    *   the [[GreenHouseDivisionController]] object.
    */
  def apply(): GreenHouseDivisionController = GreenHouseDivisionControllerImpl()

  private class GreenHouseDivisionControllerImpl extends GreenHouseDivisionController:
    val model = GreenHouse(
      List("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9", "p10", "p11", "p12"),
      List("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9", "p10", "p11", "p12"),
      "Rome"
    )
    override var view: GreenHouseDivisionView = _

    override def updateView(): Unit =
      view.paintDivision(model.dimension._1, model.dimension._2, model.areas.map((_, true)))

    override def openArea(area: (String, Boolean)): (String, Boolean) =
      println(s"open area $area")
      area
