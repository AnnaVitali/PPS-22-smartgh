package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.plants.Plant
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.Observable
import monix.reactive.MulticastStrategy
import monix.execution.Scheduler.Implicits.global

/** Implementation of the [[AreaModelModule]]. */
object AreaModelModule:
  enum AreaStatus:
    case ALARM, NORMAL
  export AreaStatus.*

  /** This trait exposes the methods for managing the area model. */
  trait AreaModel:
    def status_=(s: AreaStatus): Unit
    def status: AreaStatus
    /** Plant grown in the area. */
    val plant: Plant
    /** Sensor of the area. */
    val sensors: Map[String, Int]
    /** @return the map of the actual sensors values*/
    def sensorValues(): Map[String, Int]
    /** Method that can be called to obtain the [[Observable]] associated to the status of an area.
     * @return
     *   the [[Observable]] associated to the status of an area.
     */
    def changeStatusObservable(): Observable[AreaStatus]


  /** A trait for defining the model instance.*/
  trait Provider:
    val areaModel: AreaModel

  /** A trait that represents the area model component. */
  trait Component:
    /** Implementation of the area model.*/
    class AreaImpl(override val plant: Plant,
                  ) extends AreaModel:
      private var _status: AreaStatus = AreaStatus.NORMAL

      private val subject = ConcurrentSubject[AreaStatus](MulticastStrategy.publish)

      override def status: AreaStatus = _status

      override def status_=(s: AreaStatus): Unit =
        _status = s
        subject.onNext(_status)
      
      override val sensors: Map[String, Int] = Map("Temperature" -> 1, "Brightness" -> 2, "Humidity" -> 3, "Soil moisture" -> 4)//TODO change collection of sensors

      override def sensorValues(): Map[String, Int] =
        for s <- sensors
          yield s //TODO change with actual sensor value

      override def changeStatusObservable(): Observable[AreaStatus] = subject



  /** Trait that combine provider and component for area model.*/
  trait Interface extends Provider with Component
