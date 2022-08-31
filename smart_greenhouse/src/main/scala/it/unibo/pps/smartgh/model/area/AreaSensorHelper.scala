package it.unibo.pps.smartgh.model.area

import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaStatus.{ALARM, NORMAL}
import it.unibo.pps.smartgh.model.area.AreaModelModule.{AreaModel, AreaStatus}
import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
import it.unibo.pps.smartgh.model.sensor.*
import it.unibo.pps.smartgh.mvc.component.areaParameter.*
import monix.execution.Ack.Continue
import monix.reactive.subjects.ConcurrentSubject
import org.scalactic.TripleEquals.convertToEqualizer

/** Implementation of the [[AreaSensorHelper]]. */
object AreaSensorHelper:

  private val TemperatureKey = "Temperature"
  private val AirHumidityKey = "Humidity"
  private val SoilHumidityKey = "Soil moisture"
  private val BrightnessKey = "Brightness"

  /** Apply method for [[AreaSensorHelper]]
    * @param areaComponentState
    *   component state of the area
    * @param addTimerCallback
    *   the callback for the timer.
    * @return
    *   instance of [[AreaSensorHelperImpl]]
    */
  def apply(
      areaComponentState: AreaComponentsStateImpl,
      addTimerCallback: (f: String => Unit) => Unit
  ): AreaSensorHelperImpl =
    AreaSensorHelperImpl(areaComponentState, addTimerCallback)

  /** Class with helper methods for area sensor.
    * @param areaComponentState
    *   component state of the area.
    * @param addTimerCallback
    *   the callback for the timer.
    */
  class AreaSensorHelperImpl(
      areaComponentState: AreaComponentsStateImpl,
      addTimerCallback: (f: String => Unit) => Unit
  ):
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
      if (actualVal compareTo min) < 0 || (actualVal compareTo max) > 0 then SensorStatus.ALARM
      else SensorStatus.NORMAL

    private def constructSensorsMap[T >: Sensor](): Map[String, T] = Map(
      TemperatureKey -> TemperatureSensor(areaComponentState, addTimerCallback),
      AirHumidityKey -> AirHumiditySensor(areaComponentState, addTimerCallback),
      SoilHumidityKey -> SoilHumiditySensor(areaComponentState, addTimerCallback),
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

    def configSensors(
        sensors: List[ManageSensorImpl],
        checkAlarm: ManageSensorImpl => Unit,
        subjectComponentsState: ConcurrentSubject[AreaComponentsStateImpl, AreaComponentsStateImpl]
    ): AreaStatus =
      sensorsMap.foreach { (name, sensor) =>
        sensor.registerValueCallback(
          v => {
            val ms = sensors.find(_.name === name).orNull
            ms.actualVal = BigDecimal(v).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
            checkAlarm(ms)
            Continue
          },
          (ex: Throwable) => ex.printStackTrace(),
          () => {}
        )
        sensor.setObserverActionsArea(subjectComponentsState)
      }
      if sensors.forall(_.status === SensorStatus.NORMAL) then NORMAL
      else ALARM

    def parametersMVC(areaModel: AreaModel, updateStateMessage: (String, Boolean) => Unit): Seq[AreaParameterMVC] =
      Seq(
        AreaLuminosityMVC(areaModel, updateStateMessage),
        AreaTemperatureMVC(areaModel, updateStateMessage),
        AreaAirHumidityMVC(areaModel, updateStateMessage),
        AreaSoilMoistureMVC(areaModel, updateStateMessage)
      )
