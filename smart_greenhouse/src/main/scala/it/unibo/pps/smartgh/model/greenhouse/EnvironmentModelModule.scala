package it.unibo.pps.smartgh.model.greenhouse

import it.unibo.pps.smartgh.model.city.City
import it.unibo.pps.smartgh.model.time.TimeModel

/** Object that encloses the model module to manage ambient environment values and simulation time. */
object EnvironmentModelModule:

  /** A trait that represents the model for environment values and time management. */
  trait EnvironmentModel:

    /** Object that represent the city where the greenhouse is placed, it stores its environment values */
    val city: City

    /** Object to manage the time of the simulation, it represents its model */
    val time: TimeModel

  /** Trait that represents the provider of the model for environment values and time management. */
  trait Provider:
    val environmentModel: EnvironmentModel

  /** Trait that represents the components of the model for environment values and time management. */
  trait Component:

    /** Class that contains the [[EnvironmentModel]] implementation.
      * @param city
      *   object of the city selected by the user
      */
    class EnvironmentModelImpl(override val city: City) extends EnvironmentModel:

      override val time: TimeModel = TimeModel()

  /** Trait that encloses the model for environment values and time management. */
  trait Interface extends Provider with Component
