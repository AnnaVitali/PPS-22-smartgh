package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.AreaControllerModule
import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.view.component.AreaViewModule

object AreaMVC:

  def apply(plant: Plant): AreaMVCImpl = AreaMVCImpl(plant)

  class AreaMVCImpl(plant: Plant)
    extends AreaModelModule.Interface
    with AreaViewModule.Interface
    with AreaControllerModule.Interface:
    override val areaModel = AreaImpl(plant, AreaModelModule.AreaStatus.NORMAL)
    override val areaView = AreaViewImpl()
    override val areaController= AreaControllerImpl()

    def paintArea(): Unit = areaController.paintArea()