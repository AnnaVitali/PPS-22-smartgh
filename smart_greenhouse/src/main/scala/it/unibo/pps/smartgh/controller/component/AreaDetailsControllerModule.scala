package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.view.component.{AreaDetailsViewModule, BaseView}
import org.apache.commons.lang3.time.DurationFormatUtils

import scala.concurrent.duration.FiniteDuration

object AreaDetailsControllerModule:

  trait AreaDetailsController extends SceneController:
    def initializeView(): Unit

  trait Provider:
    val areaDetailsController: AreaDetailsController

  type Requirements = AreaDetailsViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaDetailsControllerImpl(simulationMVC: SimulationMVCImpl) extends AreaDetailsController:

      override def instantiateNextSceneMVC(baseView: BaseView): Unit = ???

      override def initializeView(): Unit =
        areaDetailsView.initializeParameters(simulationMVC, areaModel)
        val plant = areaModel.plant
        areaDetailsView.updatePlantInformation(plant.name, plant.description, plant.imageUrl, plant.optimalValues)
        areaModel.timer.addCallback(
          time => areaDetailsView.updateTime(DurationFormatUtils.formatDuration(time.toMillis, "HH:mm:ss", true)),
          1
        )

  trait Interface extends Provider with Component:
    self: Requirements =>
