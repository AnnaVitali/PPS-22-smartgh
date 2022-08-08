package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.model.sensor.{
  AirHumiditySensor,
  LuminositySensor,
  Sensor,
  SensorWithTimer,
  SoilHumiditySensor,
  TemperatureSensor
}
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
    /** @return the map of the actual sensors values */
    def sensorValues(): Map[String, Double]
    /** Method that can be called to obtain the [[Observable]] associated to the status of an area.
      * @return
      *   the [[Observable]] associated to the status of an area.
      */
    def changeStatusObservable(): Observable[AreaStatus]

    //TODO doc
    def setSensorSubjects(subjects: Map[String, ConcurrentSubject[Double, Double]]): Unit

  /** A trait for defining the model instance. */
  trait Provider:
    val areaModel: AreaModel

  /** A trait that represents the area model component. */
  trait Component:
    /** Implementation of the area model. */
    class AreaImpl(override val plant: Plant, val timer: Timer) extends AreaModel:

      private var _status: AreaStatus = AreaStatus.NORMAL
      private val subject = ConcurrentSubject[AreaStatus](MulticastStrategy.publish)
      private val optimalValueToDouble: Map[String, Double] =
        plant.optimalValues.map((k, v) => (k, v.toString.toDouble))
      private val areaComponentState = AreaComponentsState()
      private val subjectComponentsState = ConcurrentSubject[AreaComponentsStateImpl](MulticastStrategy.publish)
      private val TemperatureKey = "Temperature"
      private val AirHumidityKey = "Humidity"
      private val SoilHumidityKey = "Soil moisture"
      private val BrightnessKey = "Brightness"
      private val sensorWithTimerMap: Map[String, SensorWithTimer] = Map(
        TemperatureKey -> TemperatureSensor(areaComponentState, timer),
        AirHumidityKey -> AirHumiditySensor(areaComponentState, timer),
        SoilHumidityKey -> SoilHumiditySensor(areaComponentState, timer)
      )
      private val sensorWithoutTimerMap: Map[String, Sensor] = Map(
        BrightnessKey -> LuminositySensor(10000.0, areaComponentState)
      )

      override val sensors: List[ManageSensorImpl] =
        List(manageTemperatureSensor(), manageAirHumiditySensor(), manageSoilHumiditySensor(), manageBrightnessSensor())

      configGenericSensors(sensorWithoutTimerMap)
      configSensorsWithTimer()

      override def setSensorSubjects(subjects: Map[String, ConcurrentSubject[Double, Double]]): Unit =
        subjects.foreach((k, v) =>
          k match
            case "temp" => sensorWithTimerMap(TemperatureKey).setObserverEnvironmentValue(v)
            case "hum" => sensorWithTimerMap(AirHumidityKey).setObserverEnvironmentValue(v)
            case "lux" => sensorWithoutTimerMap(BrightnessKey).setObserverEnvironmentValue(v)
            case "soilMoist" => sensorWithTimerMap(SoilHumidityKey).setObserverEnvironmentValue(v)
        )

      override def status: AreaStatus = _status

      override def status_=(s: AreaStatus): Unit =
        _status = s
        subject.onNext(_status)

      override def sensorValues(): Map[String, Double] =
        sensors.map(ms => (ms.name, ms.actualVal)).toMap

      override def changeStatusObservable(): Observable[AreaStatus] = subject

      private def manageTemperatureSensor(): ManageSensorImpl =
        ManageSensorImpl(
          TemperatureKey,
          optimalValueToDouble.getOrElse("min_temp", 0.0),
          optimalValueToDouble.getOrElse("max_temp", 0.0),
          sensorWithTimerMap(TemperatureKey),
          sensorWithTimerMap(TemperatureKey).getCurrentValue()
        )

      private def manageAirHumiditySensor(): ManageSensorImpl =
        ManageSensorImpl(
          AirHumidityKey,
          optimalValueToDouble.getOrElse("min_env_humid", 0.0),
          optimalValueToDouble.getOrElse("max_env_humid", 0.0),
          sensorWithTimerMap(AirHumidityKey),
          sensorWithTimerMap(AirHumidityKey).getCurrentValue()
        )

      private def manageSoilHumiditySensor(): ManageSensorImpl =
        ManageSensorImpl(
          SoilHumidityKey,
          optimalValueToDouble.getOrElse("min_soil_moist", 0.0),
          optimalValueToDouble.getOrElse("max_soil_moist", 0.0),
          sensorWithTimerMap(SoilHumidityKey),
          sensorWithTimerMap(SoilHumidityKey).getCurrentValue()
        )

      private def manageBrightnessSensor(): ManageSensorImpl =
        ManageSensorImpl(
          BrightnessKey,
          optimalValueToDouble.getOrElse("min_light_lux", 0.0),
          optimalValueToDouble.getOrElse("max_light_lux", 0.0),
          sensorWithoutTimerMap(BrightnessKey),
          sensorWithoutTimerMap(BrightnessKey).getCurrentValue()
        )

      private def checkAlarm(ms: ManageSensorImpl): Unit =
        if (ms.actualVal compareTo ms.min) < 0 || (ms.actualVal compareTo ms.max) > 0 then status = ALARM

      private def configGenericSensors(sensorMap: Map[String, Sensor]): Unit =
        sensorMap.foreach { (name, sensor) =>
          sensor.registerValueCallback(
            v => {
              val ms = sensors.find(ms => ms.name == name).orNull
              ms.actualVal = BigDecimal(v).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
              checkAlarm(ms)
              Continue
            },
            (ex: Throwable) => ex.printStackTrace(),
            () => {}
          )
          sensor.setObserverActionsArea(subjectComponentsState)
        }

      private def configSensorsWithTimer(): Unit =
        configGenericSensors(sensorWithTimerMap)
        sensorWithTimerMap.foreach((n, s) => s.registerTimerCallback())

  /** Trait that combine provider and component for area model. */
  trait Interface extends Provider with Component
