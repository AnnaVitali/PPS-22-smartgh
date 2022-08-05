package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.AreaControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.view.component.AreaViewModule

/** Object that can be used to create a new instance of [[AreaMVCImpl]]. */
object AreaMVC:
  /** Create a new [[AreaMVCImpl]].
   * @return
   *   a new instance of [[AreaMVCImpl]].
   */
  def apply(plant: Plant): AreaMVCImpl = AreaMVCImpl(plant)

  /**Implementation of the area MVC*/
  class AreaMVCImpl(plant: Plant)
    extends AreaModelModule.Interface
    with AreaViewModule.Interface
    with AreaControllerModule.Interface:
    override val areaModel = AreaImpl(plant)
    override val areaView = AreaViewImpl()
    override val areaController= AreaControllerImpl()

    def paintArea(): Unit = areaController.paintArea()