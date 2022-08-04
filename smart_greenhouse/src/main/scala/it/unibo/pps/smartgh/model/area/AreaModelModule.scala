package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.plants.Plant

/** Implementation of the [[AreaModelModule]]. */
object AreaModelModule:
  enum AreaStatus:
    case ALARM, NORMAL
  export AreaStatus.*

  /** This trait exposes the methods for managing the area model. */
  trait AreaModel:
    /** Plant grown in the area. */
    val plant: Plant
    /** Plant grown in the area. */
    var status: AreaStatus
    /** Sensor of the area. */
    val sensors: Map[String, Int]

    /** @return the map of the actual sensors values*/
    def sensorValues(): Map[String, Int]

  /** A trait for defining the model instance.*/
  trait Provider:
    val areaModel: AreaModel

  /** A trait that represents the area model component. */
  trait Component:
    /** Implementation of the area model.*/
    class AreaImpl(override val plant: Plant,
                   override var status: AreaStatus
                  ) extends AreaModel:
      override val sensors: Map[String, Int] = Map("Temperature" -> 1, "Brightness" -> 2, "Humidity" -> 3, "Soil moisture" -> 4)//TODO change collection of sensors

      override def sensorValues(): Map[String, Int] =
        for s <- sensors
          yield s //TODO change with actual sensor value


  /** Trait that combine provider and component for area model.*/
  trait Interface extends Provider with Component
