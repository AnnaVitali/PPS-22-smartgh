package it.unibo.pps.smartgh.model.greenhouse

import monix.execution.Scheduler.Implicits.global
import monix.execution.{Ack, Cancelable}
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject

/** Object that encloses the model module to manage ambient environment values and simulation time. */
object EnvironmentModelModule:

  /** A trait that represents the model for environment values and time management. */
  trait EnvironmentModel:

    /** Object that represent the city where the greenhouse is placed, it stores its [[Environment]] values. */
    val environment: Environment

    /** set subject for Temperature sensor [[it.unibo.pps.smartgh.model.sensor.TemperatureSensor]]. */
    val subjectTemperature: ConcurrentSubject[Double, Double]

    /** set subject for Humidity sensor [[it.unibo.pps.smartgh.model.sensor.SoilHumiditySensor]]. */
    val subjectHumidity: ConcurrentSubject[Double, Double]

    /** set subject for Luminosity sensor [[it.unibo.pps.smartgh.model.sensor.LuminositySensor]]. */
    val subjectLuminosity: ConcurrentSubject[Double, Double]

    /** set subject for soil moisture sensor [[it.unibo.pps.smartgh.model.sensor.SoilHumiditySensor]]. */
    val subjectSoilMoisture: ConcurrentSubject[Double, Double]

  /** Trait that represents the provider of the model for environment values and time management. */
  trait Provider:
    /** The environment model. */
    val environmentModel: EnvironmentModel

  /** Trait that represents the components of the model for environment values and time management. */
  trait Component:

    /** Class that contains the [[EnvironmentModel]] implementation.
      * @param environment
      *   object of the city selected by the user
      */
    class EnvironmentModelImpl(override val environment: Environment) extends EnvironmentModel:

      override val subjectTemperature: ConcurrentSubject[Double, Double] =
        ConcurrentSubject[Double](MulticastStrategy.publish)
      override val subjectHumidity: ConcurrentSubject[Double, Double] =
        ConcurrentSubject[Double](MulticastStrategy.publish)
      override val subjectLuminosity: ConcurrentSubject[Double, Double] =
        ConcurrentSubject[Double](MulticastStrategy.publish)
      override val subjectSoilMoisture: ConcurrentSubject[Double, Double] =
        ConcurrentSubject[Double](MulticastStrategy.publish)

  /** Trait that encloses the model for environment values and time management. */
  trait Interface extends Provider with Component
