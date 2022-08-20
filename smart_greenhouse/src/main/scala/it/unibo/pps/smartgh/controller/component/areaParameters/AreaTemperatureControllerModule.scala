package it.unibo.pps.smartgh.controller.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaParametersController.{
  AbstractAreaParametersController,
  AreaParametersController
}
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaModelModule}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import it.unibo.pps.smartgh.view.component.areaParameters.AreaTemperatureViewModule

object AreaTemperatureControllerModule:

  trait AreaTemperatureController extends AreaParametersController:

    /** Get the current temperature.
      * @return
      *   the temperature value
      */
    def temperature: Double

    /** Update the temperature inside the area.
      * @param value
      *   New temperature value
      */
    def updTempValue(value: Double): Unit

    /** Specifies whether the gates is open.
      * @return
      *   true if the gates is open, false otherwise
      */
    def isGatesOpen: Boolean

    /** Open the area gates. */
    def openGates(): Unit

    /** Close the area gates. */
    def closeGates(): Unit

  trait Provider:
    val parameterController: AreaParametersController

  type Requirements = AreaTemperatureViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaTemperatureControllerImpl(updateStateMessage: (String, Boolean) => Unit)
        extends AbstractAreaParametersController("Temperature", areaModel, updateStateMessage)
        with AreaTemperatureController:

      override def temperature: Double = areaModel.temperature
      override def updTempValue(value: Double): Unit = areaModel.updTemperature(value)
      override def isGatesOpen: Boolean = areaModel.gatesState == AreaGatesState.Open
      override def openGates(): Unit = areaModel.updGateState(AreaGatesState.Open)
      override def closeGates(): Unit = areaModel.updGateState(AreaGatesState.Close)

  trait Interface extends Provider with Component:
    self: Requirements =>
