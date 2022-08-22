package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaStatus
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaStatus.{ALARM, NORMAL}
import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
import it.unibo.pps.smartgh.model.sensor.{AirHumiditySensor, LuminositySensor, Sensor, SensorStatus, SensorWithTimer, SoilHumiditySensor, TemperatureSensor}
import monix.execution.Ack.Continue
import monix.reactive.subjects.ConcurrentSubject
import it.unibo.pps.smartgh.model.time.Timer
import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
/** Implementation of the [[AreaSensorHelper]]. */
object AreaSensorHelper:
  /**
    * Apply method for [[AreaSensorHelper]]
    * @param areaComponentState component state of the area
    * @param timer simulation [[Timer]]
    * @return instance of [[AreaSensorHelperImpl]]
    */
  def apply(areaComponentState: AreaComponentsStateImpl, timer: Timer): AreaSensorHelperImpl = AreaSensorHelperImpl(areaComponentState, timer)

  /**
    * Class with helper methods for area sensor
    * @param areaComponentState component state of the area
    * @param timer simulation [[Timer]]
    */
  class AreaSensorHelperImpl(areaComponentState: AreaComponentsStateImpl, timer: Timer):
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

    private def firstSensorStatus(actualVal: Double, min: Double, max: Double): SensorStatus =
      if (actualVal compareTo min) < 0 || (actualVal compareTo max) > 0 then
        SensorStatus.ALARM
      else
        SensorStatus.NORMAL

    private def constructSensorsMap[T >: Sensor](): Map[String, T] = Map(
      TemperatureKey -> TemperatureSensor(areaComponentState, timer),
      AirHumidityKey -> AirHumiditySensor(areaComponentState, timer),
      SoilHumidityKey -> SoilHumiditySensor(areaComponentState, timer),
      BrightnessKey -> LuminositySensor(10000.0, areaComponentState)
    )

    def manageSensorList(optimalValueToDouble: Map[String, Double]): List[ManageSensorImpl] =
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
        firstSensorStatus(
          BigDecimal(sensorsMap(key).getCurrentValue).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble,
          optimalValueToDouble.getOrElse("min_" + optK, 0.0),
          optimalValueToDouble.getOrElse("max_" + optK, 0.0)
        )
      )

    def setSensorSubjects(subjects: Map[String, ConcurrentSubject[Double, Double]]): Unit =
      subjects.foreach((k, v) =>
        k match
          case "temp" => sensorsMap(TemperatureKey).setObserverEnvironmentValue(v)
          case "hum" => sensorsMap(AirHumidityKey).setObserverEnvironmentValue(v)
          case "lux" => sensorsMap(BrightnessKey).setObserverEnvironmentValue(v)
          case "soilMoist" => sensorsMap(SoilHumidityKey).setObserverEnvironmentValue(v)
      )

    def configSensors(sensors: List[ManageSensorImpl],
                      checkAlarm: ManageSensorImpl => Unit,
                      subjectComponentsState: ConcurrentSubject[AreaComponentsStateImpl, AreaComponentsStateImpl]): AreaStatus =
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
      if sensors.forall(ms => ms.status == SensorStatus.NORMAL) then
        NORMAL
      else
        ALARM
