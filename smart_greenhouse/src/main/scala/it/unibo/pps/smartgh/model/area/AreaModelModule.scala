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
import it.unibo.pps.smartgh.model.area.AreaComponentsState
import it.unibo.pps.smartgh.model.area.AreaGatesState
import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import monix.reactive.subjects.ConcurrentSubject
import monix.reactive.MulticastStrategy.Behavior
import monix.reactive.Observable
import monix.reactive.MulticastStrategy
import monix.execution.Scheduler.Implicits.global
import it.unibo.pps.smartgh.model.time.Timer
import monix.execution.Ack.{Continue, Stop}
import it.unibo.pps.smartgh.model.sensor.SensorStatus
import monix.eval.Task

import scala.math.BigDecimal

/** Implementation of the [[AreaModelModule]]. */
object AreaModelModule:
  enum AreaStatus:
    case ALARM, NORMAL
  export AreaStatus.*

  /** This trait exposes the methods for managing the area model. */
  trait AreaModel:
    /** Set the status of the area.
      * @param s
      *   new [[AreaStatus]]
      */
    def status_=(s: AreaStatus): Unit
    /** Status of the area. */
    def status: AreaStatus
    /** [[Plant]] grown in the area. */
    val plant: Plant
    /** Simulation [[Timer]]. */
    val timer: Timer
    /** Sensor of the area. */
    val sensors: List[ManageSensorImpl]
    /** @return the map of the actual sensors values */
    def sensorValues(): Map[String, Double]
    /** Method that can be called to obtain the [[Observable]] associated to the status of an area.
      * @return
      *   the [[Observable]] associated to the status of an area.
      */
    def changeStatusObservable(): Observable[AreaStatus]

    /** Set the sensors' subjects
      * @param subjects
      *   the subject of the sensor.
      */
    def setSensorSubjects(subjects: Map[String, ConcurrentSubject[Double, Double]]): Unit

    /** Update the [[AreaGatesState]] due to a user action.
      * @param state
      *   the new state for the gate
      */
    def updGateState(state: AreaGatesState): Unit

    /** Get the [[AreaGatesState]].
      * @return
      *   the gates state
      */
    def gatesState: AreaGatesState

    /** Update the [[AreaShieldState]]due to a user action.
      * @param state
      *   new shield state
      */
    def updShieldState(state: AreaShieldState): Unit

    /** Get the shielded information.
      * @return
      *   if the area is shielded or not
      */
    def isShielded: Boolean

    /** Get the atomiser state.
      * @return
      *   the [[AreaAtomiseState]].
      */
    def atomiserState: AreaAtomiseState

    /** Update the [[AreaAtomiseState]] due to a user action.
      * @param state
      *   new atomize state
      */
    def updAtomizeState(state: AreaAtomiseState): Unit

    /** Get the ventilation state.
      * @return
      *   the [[AreaVentilationState]].
      */
    def ventilationState: AreaVentilationState

    /** Update the [[AreaVentilationState]] due to a user action.
      * @param state
      *   new ventilation state
      */
    def updVentilationState(state: AreaVentilationState): Unit

    /** Update the [[AreaHumidityState]] due to a user action.
      * @param state
      *   new humidity state
      */
    def updHumidityAction(state: AreaHumidityState): Unit

    /** Update the lamps' brightness due to a user action.
      * @param value
      *   new brightness value
      */
    def updBrightnessOfLamp(value: Double): Unit

    /** Get the current lamps' brightness.
      * @return
      *   the lamp brightness value
      */
    def getBrightnessOfLamp: Double

    /** Get the current temperature.
      * @return
      *   the temperature value
      */
    def temperature: Double

    /** Update the temperature value due to a user action.
      * @param value
      *   new temperature value
      */
    def updTemperature(value: Double): Unit

    /** Get a copy of the area's component
      * @return
      *   the area's component
      */
    def getAreaComponent: AreaComponentsStateImpl

  /** A trait for defining the model instance. */
  trait Provider:
    /** The area model. */
    val areaModel: AreaModel

  /** A trait that represents the area model component. */
  trait Component:
    /** Implementation of the area model.
      * @param plant
      *   [[Plant]] grown in the area.
      * @param timer
      *   instance of the simulation [[Timer]].
      */
    class AreaImpl(override val plant: Plant, override val timer: Timer) extends AreaModel:

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
      private val mapSensorNamesAndMessages: Map[String, Map[String, String]] = Map(
        TemperatureKey -> Map(
          ("name", "temp"),
          ("um", "°"),
          (
            "message",
            "Adjust the indoor temperature by also considering the outdoor temperature, to manage it better you could " +
              "close the greenhouse (and screen it) and set the indoor temperature"
          )
        ),
        AirHumidityKey -> Map(
          ("name", "env_humid"),
          ("um", "%"),
          (
            "message",
            "Adjust the indoor humidity considering also the outdoor humidity, also you can ventilate the area to decrease " +
              "it or vaporize to increase it"
          )
        ),
        SoilHumidityKey -> Map(
          "name" -> "soil_moist",
          ("um", "%"),
          (
            "message",
            "Adjust soil moisture considering weather conditions, also decrease it by loosening the soil or increase it by watering"
          )
        ),
        BrightnessKey -> Map(
          ("name", "light_lux"),
          ("um", "Lux"),
          (
            "message",
            "Adjust the indoor brightness considering the outdoor brightness, also you can shield the area " +
              "and/or adjust the brightness of the lamps"
          )
        )
      )

      private val sensorsMap = constructSensorsMap()

      override val sensors: List[ManageSensorImpl] =
        for
          (key, m) <- mapSensorNamesAndMessages.toList
          optK = m.getOrElse("name", "")
          um = m.getOrElse("um", "")
          msg = m.getOrElse("message", "")
        yield ManageSensorImpl(
          key,
          optimalValueToDouble.getOrElse("min_" + optK, 0.0),
          optimalValueToDouble.getOrElse("max_" + optK, 0.0),
          um,
          sensorsMap(key),
          BigDecimal(sensorsMap(key).getCurrentValue).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble,
          msg,
          SensorStatus.NORMAL
        )

      configSensors()

      override def setSensorSubjects(subjects: Map[String, ConcurrentSubject[Double, Double]]): Unit =
        subjects.foreach((k, v) =>
          k match
            case "temp" => sensorsMap(TemperatureKey).setObserverEnvironmentValue(v)
            case "hum" => sensorsMap(AirHumidityKey).setObserverEnvironmentValue(v)
            case "lux" => sensorsMap(BrightnessKey).setObserverEnvironmentValue(v)
            case "soilMoist" => sensorsMap(SoilHumidityKey).setObserverEnvironmentValue(v)
        )

      override def status: AreaStatus = _status

      override def status_=(s: AreaStatus): Unit =
        Task {
          _status = s
          subject.onNext(_status)
        }.executeAsync.runToFuture

      override def sensorValues(): Map[String, Double] =
        sensors.map(ms => (ms.name, ms.actualVal)).toMap

      override def changeStatusObservable(): Observable[AreaStatus] = subject

      override def temperature: Double = areaComponentState.temperature

      override def updTemperature(value: Double): Unit =
        areaComponentState.temperature = value
        notifySensorStatusChange()

      override def updBrightnessOfLamp(value: Double): Unit =
        areaComponentState.brightnessOfTheLamps = value
        notifySensorStatusChange()

      override def getBrightnessOfLamp: Double = areaComponentState.brightnessOfTheLamps

      override def updHumidityAction(state: AreaHumidityState): Unit =
        areaComponentState.humidityActions = state
        notifySensorStatusChange()

      override def atomiserState: AreaAtomiseState = areaComponentState.atomisingState

      override def updAtomizeState(state: AreaAtomiseState): Unit =
        areaComponentState.atomisingState = state
        notifySensorStatusChange()

      override def gatesState: AreaGatesState = areaComponentState.gatesState

      override def updGateState(state: AreaGatesState): Unit =
        areaComponentState.gatesState = state
        notifySensorStatusChange()

      override def updShieldState(state: AreaShieldState): Unit =
        areaComponentState.shieldState = state
        notifySensorStatusChange()

      override def isShielded: Boolean = areaComponentState.shieldState == AreaShieldState.Down

      override def ventilationState: AreaVentilationState = areaComponentState.ventilationState

      override def updVentilationState(state: AreaVentilationState): Unit =
        areaComponentState.ventilationState = state
        notifySensorStatusChange()

      override def getAreaComponent: AreaComponentsStateImpl = areaComponentState.copy()

      private def notifySensorStatusChange(): Unit =
        subjectComponentsState.onNext(areaComponentState)

      private def checkAlarm(ms: ManageSensorImpl): Unit =
        if (ms.actualVal compareTo ms.min) < 0 || (ms.actualVal compareTo ms.max) > 0 then
          if status == NORMAL then status = ALARM
          ms.status = SensorStatus.ALARM
        else
          ms.status = SensorStatus.NORMAL
          if sensors.forall(ms => ms.status == SensorStatus.NORMAL) then status = NORMAL

      private def configSensors(): Unit =
        sensorsMap.foreach { (name, sensor) =>
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
          sensor match
            case sensorWithTimer: SensorWithTimer => sensorWithTimer.registerTimerCallback()
            case _ =>
        }

      private def constructSensorsMap[T >: Sensor](): Map[String, T] = Map(
        TemperatureKey -> TemperatureSensor(areaComponentState, timer),
        AirHumidityKey -> AirHumiditySensor(areaComponentState, timer),
        SoilHumidityKey -> SoilHumiditySensor(areaComponentState, timer),
        BrightnessKey -> LuminositySensor(10000.0, areaComponentState)
      )

  /** Trait that combine provider and component for area model. */
  trait Interface extends Provider with Component
