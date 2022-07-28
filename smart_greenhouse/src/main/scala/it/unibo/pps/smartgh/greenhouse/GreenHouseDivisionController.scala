package it.unibo.pps.smartgh.greenhouse

import it.unibo.pps.smartgh.view.component.GreenHouseDivisionView

trait GreenHouseDivisionController:
  /**update division view */
  def updateView():Unit

  /** open the selected area
   * @return the selected area*/
  def openArea(area: (String, Boolean)): (String, Boolean)

  /** @return the view instance*/
  def view: GreenHouseDivisionView

  /** set the view reference
   * @param  view instance*/
  def view_= (view: GreenHouseDivisionView): Unit


object GreenHouseDivisionController:
  def apply(): GreenHouseDivisionControllerImpl = GreenHouseDivisionControllerImpl()

  class GreenHouseDivisionControllerImpl extends GreenHouseDivisionController:
    val model = GreenHouse(List("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9", "p10", "p11", "p12"),
      List("p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9", "p10", "p11", "p12"),
      "Rome")
    var _view: GreenHouseDivisionView = null

    override def view: GreenHouseDivisionView = _view
    override def view_= (view: GreenHouseDivisionView): Unit = _view = view

    override def updateView(): Unit = view.paintDivision(model.dimension._1, model.dimension._2, model.areas.map((_, true)))

    override def openArea(area: (String, Boolean)): (String, Boolean) =
      println(s"open area $area")
      area




