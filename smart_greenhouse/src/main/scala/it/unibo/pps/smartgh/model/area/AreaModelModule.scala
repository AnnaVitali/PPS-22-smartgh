package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
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
    val sensors: List[ManageSensorImpl]
    /** @return the map of the actual sensors values*/
    def sensorValues(): Map[String, Float]
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
      private val optimalValueToFloat: Map[String, Float] = plant.optimalValues.map((k, v) => (k, v.toString.toFloat))

      private def checkAlarm(ms: ManageSensorImpl): Unit =
        if (ms.actualVal compareTo ms.min ) < 0 ||  (ms.actualVal compareTo ms.max) > 0 then
            status = ALARM

      override val sensors: List[ManageSensorImpl] =
        List(
          ManageSensorImpl("Temperature",
            optimalValueToFloat.getOrElse("min_temp", 0),
            optimalValueToFloat.getOrElse("max_temp", 0),
            1, // TODO change with new TemperatureSensor
            15), // TODO change with optimal value for starting
          ManageSensorImpl("Humidity",
            optimalValueToFloat.getOrElse("min_env_humid", 0),
            optimalValueToFloat.getOrElse("max_env_humid", 0),
            2,
            50),
          ManageSensorImpl("Soil moisture",
            optimalValueToFloat.getOrElse("min_soil_moist", 0),
            optimalValueToFloat.getOrElse("max_soil_moist", 0),
            3,
            30),
          ManageSensorImpl("Brightness",
            optimalValueToFloat.getOrElse("min_light_lux", 0),
            optimalValueToFloat.getOrElse("max_light_lux", 0),
            4,
            5000)
        )

      override def status: AreaStatus = _status

      override def status_=(s: AreaStatus): Unit =
        _status = s
        subject.onNext(_status)

      override def sensorValues(): Map[String, Float] =
        sensors.map(ms => (ms.name, ms.actualVal)).toMap

      override def changeStatusObservable(): Observable[AreaStatus] = subject

      sensors.foreach(ms =>
        //TODO sensor subscribe -> in the subscribe upd the ms actual value and check ALARM
        checkAlarm(ms)
        println(ms.name)
      )



  /** Trait that combine provider and component for area model.*/
  trait Interface extends Provider with Component
