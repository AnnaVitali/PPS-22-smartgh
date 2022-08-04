package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.plants.Plant

/** Implementation of the [[AreaModelModule]]. */
object AreaModelModule:
  enum AreaStatus:
    case ALARM, NORMAL
  export AreaStatus.*

  /** This trait exposes the methods for managing the area model. */
  trait Model:
    /** Plant grown in the area. */
    val plant: Plant
    /** Plant grown in the area. */
    var status: AreaStatus
    /** Sensor of the area. */
    val sensors: List[Int]

  /** A trait for defining the model instance.*/
  trait Provider:
    val areaModel: Model

  /** A trait that represents the area model component. */
  trait Component:
    /** Implementation of the area model.*/
    class AreaImpl(override val plant: Plant,
                   override var status: AreaStatus
                  ) extends Model:
      override val sensors: List[Int] = List(1,2,3,4) //TODO change to list of sensors

  /** Trait that combine provider and component for area model.*/
  trait Interface extends Provider with Component
