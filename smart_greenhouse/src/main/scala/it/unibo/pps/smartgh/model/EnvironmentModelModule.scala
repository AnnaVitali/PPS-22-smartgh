package it.unibo.pps.smartgh.model

import it.unibo.pps.smartgh.model.city.City
import it.unibo.pps.smartgh.model.time.TimeModel

/** Object that can be used to create new instances of [[EnvironmentModel]]. */
object EnvironmentModelModule:

  /** A trait that represents the global greenhouse's model of the application. */
  trait EnvironmentModel:

    /** Object that represent the city where the greenhouse is placed and where are stored its environment values */
    val city : City

    /** Object to manage the time of the simulation, it represents its model */
    val time : TimeModel

  trait Provider:
    val model : EnvironmentModel

  trait Component:
    class EnvironmentModelImpl(override val city: City) extends EnvironmentModel:

      override val time: TimeModel = TimeModel()
  

  trait Interface extends Provider with Component


