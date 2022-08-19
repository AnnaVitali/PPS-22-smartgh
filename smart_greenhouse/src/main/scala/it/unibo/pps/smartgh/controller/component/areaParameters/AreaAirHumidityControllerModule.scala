package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule
import it.unibo.pps.smartgh.model.area.{AreaAtomiseState, AreaVentilationState}

object AreaAirHumidityControllerModule:

  trait AreaAirHumidityControllerController extends AreaParameterController:
    /** Activate the area ventilation. */
    def activateVentilation(): Unit

    /** Deactivate the area ventilation. */
    def deactivateVentilation(): Unit

    /** Atomise the area */
    def atomiseArea(): Unit

    /** Disable the atomisation of the area */
    def disableAtomiseArea(): Unit

  trait Provider:
    val areaAirHumidityController: AreaAirHumidityControllerController

  type Requirements = AreaAirHumidityViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaAirHumidityControllerImpl() extends AreaAirHumidityControllerController:
      private val humSensor = areaModel.sensors.find(_.name.contentEquals("Humidity")).orNull

      override def getOptimalValues: (Double, Double) = (humSensor.min, humSensor.max)

      override def activateVentilation(): Unit =
        areaModel.updVentilationState(AreaVentilationState.VentilationActive)

      override def deactivateVentilation(): Unit =
        areaModel.updVentilationState(AreaVentilationState.VentilationInactive)

      override def atomiseArea(): Unit =
        areaModel.updAtomizeState(AreaAtomiseState.AtomisingActive)

      override def disableAtomiseArea(): Unit =
        areaModel.updAtomizeState(AreaAtomiseState.AtomisingInactive)

  trait Interface extends Provider with Component:
    self: Requirements =>
