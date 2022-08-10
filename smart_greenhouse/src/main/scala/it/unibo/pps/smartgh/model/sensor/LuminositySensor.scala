package it.unibo.pps.smartgh.model.sensor

import com.sun.javafx.webkit.theme.ContextMenuImpl
import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsLuminosity
import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.sensor.AbstractSensor
import monix.execution.Ack
import monix.reactive.Observable

import scala.util.Random
import monix.execution.Scheduler.Implicits.global
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.MulticastStrategy
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaShieldState}
import it.unibo.pps.smartgh.model.area.AreaComponentsState.*
import monix.execution.Ack.Continue

import scala.concurrent.Future

/** Object that enclose the implementation of the luminosity sensor. */
object LuminositySensor:

  /** Apply method for the [[LuminositySensorImpl]]
    * @param initialLuminosity
    *   the initial value detected by the environment for the brightness.
    * @param areaComponentsState
    *   the actual state of the area components.
    * @return
    *   the sensor responsible for detecting the brightness of the area.
    */
  def apply(initialLuminosity: Double, areaComponentsState: AreaComponentsStateImpl): LuminositySensorImpl =
    LuminositySensorImpl(initialLuminosity, areaComponentsState)

  /** Class that represents the luminosity sensor of an area of the greenhouse.
    * @param initialLuminosity
    *   the initial value detected by the environment for the brightness.
    * @param areaComponentsState
    *   the actual state of the area components.
    */
  class LuminositySensorImpl(initialLuminosity: Double, areaComponentsState: AreaComponentsStateImpl)
      extends AbstractSensor(areaComponentsState):
    private val minPercentage = 0.01
    private val maxPercentage = 0.05
    currentValue = initialLuminosity - (Random
      .nextDouble() * (maxPercentage - minPercentage) + minPercentage) * initialLuminosity

    override def computeNextSensorValue(): Unit =
      areaComponentsState.gatesState match
        case AreaGatesState.Open =>
          currentValue = FactoryFunctionsLuminosity.updateLuminosityWithAreaGatesOpen(
            currentEnvironmentValue,
            areaComponentsState.brightnessOfTheLamps
          )
        case AreaGatesState.Close if areaComponentsState.shieldState.equals(AreaShieldState.Down) =>
          currentValue = FactoryFunctionsLuminosity.updateLuminosityWithAreaGatesCloseAndShielded(
            areaComponentsState.brightnessOfTheLamps
          )
        case AreaGatesState.Close if areaComponentsState.shieldState.equals(AreaShieldState.Up) =>
          currentValue = FactoryFunctionsLuminosity.updateLuminosityWithAreaGatesCloseAndUnshielded(
            currentEnvironmentValue,
            areaComponentsState.brightnessOfTheLamps
          )
        case _ =>
      subject.onNext(currentValue)
