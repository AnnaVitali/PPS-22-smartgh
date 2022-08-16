package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.model.area.AreaModelModule
import it.unibo.pps.smartgh.view.component.{AreaLuminosityViewModule, BaseView}
import it.unibo.pps.smartgh.model.area.AreaShieldState

object AreaLuminosityControllerModule:

  trait AreaLuminosityController extends SceneController:
    /** Update the lamp brightness value
      * @param value
      *   that represents the lamp brightness
      */
    def updLampValue(value: Double): Unit

    /** Put down the area shields */
    def shieldsDown(): Unit

    /** Put up the area shields */
    def shieldsUp(): Unit

  trait Provider:
    val areaLuminosityController: AreaLuminosityController

  type Requirements = AreaLuminosityViewModule.Provider with AreaModelModule.Provider

  trait Component:
    context: Requirements =>

    class AreaLuminosityControllerImpl() extends AreaLuminosityController:
      private val lumSensor = areaModel.sensors.find(ms => ms.name == "Brightness").orNull

      override def instantiateNextSceneMVC(baseView: BaseView): Unit = ???

      override def updLampValue(value: Double): Unit = areaModel.updBrightnessOfLamp(value)

      override def shieldsDown(): Unit = areaModel.updShieldState(AreaShieldState.Down)

      override def shieldsUp(): Unit = areaModel.updShieldState(AreaShieldState.Up)

  trait Interface extends Provider with Component:
    self: Requirements =>
