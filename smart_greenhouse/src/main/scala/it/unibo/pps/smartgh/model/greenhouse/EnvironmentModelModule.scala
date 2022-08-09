package it.unibo.pps.smartgh.model.greenhouse

import it.unibo.pps.smartgh.model.city.Environment
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject
import monix.execution.Scheduler.Implicits.global

/** Object that encloses the model module to manage ambient environment values and simulation time. */
object EnvironmentModelModule:

  /** A trait that represents the model for environment values and time management. */
  trait EnvironmentModel:

    /** Object that represent the city where the greenhouse is placed, it stores its environment values */
    val environment: Environment

    //TODO add doc
    val subjectTemperature: ConcurrentSubject[Double, Double]
    val subjectHumidity: ConcurrentSubject[Double, Double]
    val subjectLuminosity: ConcurrentSubject[Double, Double]
    val subjectSoilMoisture: ConcurrentSubject[Double, Double]

  /** Trait that represents the provider of the model for environment values and time management. */
  trait Provider:
    val environmentModel: EnvironmentModel

  /** Trait that represents the components of the model for environment values and time management. */
  trait Component:

    /** Class that contains the [[EnvironmentModel]] implementation.
      * @param environment
      *   object of the city selected by the user
      */
    class EnvironmentModelImpl(override val environment: Environment) extends EnvironmentModel:

      override val subjectTemperature = ConcurrentSubject[Double](MulticastStrategy.publish)

      override val subjectHumidity = ConcurrentSubject[Double](MulticastStrategy.publish)
      override val subjectLuminosity = ConcurrentSubject[Double](MulticastStrategy.publish)
      override val subjectSoilMoisture = ConcurrentSubject[Double](MulticastStrategy.publish)

  /** Trait that encloses the model for environment values and time management. */
  trait Interface extends Provider with Component
