package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.sensor.{AirHumiditySensor, LuminositySensor, SoilHumiditySensor, TemperatureSensor}
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaGatesState
import it.unibo.pps.smartgh.model.sensor.areaComponentsState.AreaComponentsState.AreaComponentsStateImpl
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.Observable
import monix.reactive.MulticastStrategy
import monix.execution.Scheduler.Implicits.global
import it.unibo.pps.smartgh.model.time.Timer
import monix.execution.Ack.{Continue, Stop}

import scala.math.BigDecimal

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
    def sensorValues(): Map[String, Double]
    /** Method that can be called to obtain the [[Observable]] associated to the status of an area.
     * @return
     *   the [[Observable]] associated to the status of an area.
     */
    def changeStatusObservable(): Observable[AreaStatus]

    //TODO doc
    def setSensorSubjects(subjects: Map[String, ConcurrentSubject[Double, Double]]): Unit


  /** A trait for defining the model instance.*/
  trait Provider:
    val areaModel: AreaModel

  /** A trait that represents the area model component. */
  trait Component:
    /** Implementation of the area model.*/
    class AreaImpl(override val plant: Plant, val timer: Timer) extends AreaModel:

      private var _status: AreaStatus = AreaStatus.NORMAL
      private val subject = ConcurrentSubject[AreaStatus](MulticastStrategy.publish)
      private val optimalValueToDouble: Map[String, Double] = plant.optimalValues.map((k, v) => (k, v.toString.toDouble))
      private val areaComponentState = AreaComponentsState()
      private val subjectComponentsState = ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)
      
      private val temperatureSensor = TemperatureSensor(areaComponentState, timer)
      private val humiditySensor = AirHumiditySensor(areaComponentState, timer)
      private val luminositySensor = LuminositySensor(10000.0, areaComponentState) //TODO
      private val soilMoistureSensor = SoilHumiditySensor(areaComponentState.soilHumidity, areaComponentState, timer)

      configSensors()

      private def configSensors(): Unit =
        //Temperature config
        temperatureSensor.registerTimerCallback()
        temperatureSensor.registerValueCallback(
          v => {
            val ms = sensors.find(ms => ms.name == "Temperature").orNull
            ms.actualVal = BigDecimal(v).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
            checkAlarm(ms)
            Continue
          },
          (ex: Throwable) => ex.printStackTrace(),
          () => {}
        )
        temperatureSensor.setObserverActionsArea(subjectComponentsState)
        //luminosity config
        luminositySensor.registerValueCallback(
          v => {
            val ms = sensors.find(ms => ms.name == "Brightness").orNull
            ms.actualVal = BigDecimal(v).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
            checkAlarm(ms)
            Continue
          },
          (ex: Throwable) => ex.printStackTrace(),
          () => {}
        )
        luminositySensor.setObserverActionsArea(subjectComponentsState)
        //humidity config
        humiditySensor.registerTimerCallback()
        humiditySensor.registerValueCallback(
          v => {
            val ms = sensors.find(ms => ms.name == "Humidity").orNull
            ms.actualVal = BigDecimal(v).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
            checkAlarm(ms)
            Continue
          },
          (ex: Throwable) => ex.printStackTrace(),
          () => {}
        )
        humiditySensor.setObserverActionsArea(subjectComponentsState)
        //soil moisture config
        soilMoistureSensor.registerTimerCallback()
        soilMoistureSensor.registerValueCallback(
          v => {
            val ms = sensors.find(ms => ms.name == "Soil moisture").orNull
            ms.actualVal = BigDecimal(v).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
            checkAlarm(ms)
            Continue
          },
          (ex: Throwable) => ex.printStackTrace(),
          () => {}
        )
        soilMoistureSensor.setObserverActionsArea(subjectComponentsState)

      override def setSensorSubjects(subjects: Map[String, ConcurrentSubject[Double, Double]]): Unit =
        subjects.foreach((k,v) =>
          k match
            case "temp" => temperatureSensor.setObserverEnvironmentValue(v)
            case "hum" => humiditySensor.setObserverEnvironmentValue(v)
            case "lux" => luminositySensor.setObserverEnvironmentValue(v)
            case "soilMoist" => soilMoistureSensor.setObserverEnvironmentValue(v)
        )


      private def checkAlarm(ms: ManageSensorImpl): Unit =
        if (ms.actualVal compareTo ms.min ) < 0 ||  (ms.actualVal compareTo ms.max) > 0 then
            status = ALARM

      override val sensors: List[ManageSensorImpl] =
        List(
          ManageSensorImpl("Temperature",
            optimalValueToDouble.getOrElse("min_temp", 0.0),
            optimalValueToDouble.getOrElse("max_temp", 0.0),
            temperatureSensor,
            temperatureSensor.getCurrentValue()),
          ManageSensorImpl("Humidity",
            optimalValueToDouble.getOrElse("min_env_humid", 0.0),
            optimalValueToDouble.getOrElse("max_env_humid", 0.0),
            humiditySensor,
            humiditySensor.getCurrentValue()),
          ManageSensorImpl("Soil moisture",
            optimalValueToDouble.getOrElse("min_soil_moist", 0.0),
            optimalValueToDouble.getOrElse("max_soil_moist", 0.0),
            soilMoistureSensor,
            soilMoistureSensor.getCurrentValue()),
          ManageSensorImpl("Brightness",
            optimalValueToDouble.getOrElse("min_light_lux", 0.0),
            optimalValueToDouble.getOrElse("max_light_lux", 0.0),
            luminositySensor,
            luminositySensor.getCurrentValue())
        )

      override def status: AreaStatus = _status

      override def status_=(s: AreaStatus): Unit =
        _status = s
        subject.onNext(_status)

      override def sensorValues(): Map[String, Double] =
        sensors.map(ms => (ms.name, ms.actualVal)).toMap

      override def changeStatusObservable(): Observable[AreaStatus] = subject



  /** Trait that combine provider and component for area model.*/
  trait Interface extends Provider with Component
