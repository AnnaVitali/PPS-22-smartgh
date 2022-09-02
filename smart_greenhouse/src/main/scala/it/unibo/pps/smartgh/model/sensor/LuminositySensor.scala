package it.unibo.pps.smartgh.model.sensor

import it.unibo.pps.smartgh.model.area.AreaComponentsState.*
import it.unibo.pps.smartgh.model.area.{AreaGatesState, AreaShieldState}
import it.unibo.pps.smartgh.model.sensor.AbstractSensor
import monix.eval.Task
import monix.execution.Ack
import monix.execution.Scheduler.Implicits.global
import monix.reactive.MulticastStrategy

import scala.concurrent.Future
import scala.util.Random

/** Object that enclose the implementation of the luminosity sensor. */
object LuminositySensor:

  private val MinPercentage = 0.01
  private val MaxPercentage = 0.05

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

    currentValue = initialLuminosity - (Random
      .nextDouble() * (MaxPercentage - MinPercentage) + MinPercentage) * initialLuminosity

    override def computeNextSensorValue(): Unit =
      import it.unibo.pps.smartgh.model.sensor.factoryFunctions.FactoryFunctionsLuminosity.*
      Task {
        currentValue = areaComponentsState.gatesState match
          case AreaGatesState.Open =>
            updateLuminosityWithAreaGatesOpen(currentEnvironmentValue, areaComponentsState.brightnessOfTheLamps)
          case AreaGatesState.Close =>
            areaComponentsState.shieldState match
              case AreaShieldState.Down =>
                updateLuminosityWithAreaGatesCloseAndShielded(areaComponentsState.brightnessOfTheLamps)
              case AreaShieldState.Up =>
                updateLuminosityWithAreaGatesCloseAndUnshielded(
                  currentEnvironmentValue,
                  areaComponentsState.brightnessOfTheLamps
                )
        subject.onNext(currentValue)
      }.executeAsync.runToFuture
