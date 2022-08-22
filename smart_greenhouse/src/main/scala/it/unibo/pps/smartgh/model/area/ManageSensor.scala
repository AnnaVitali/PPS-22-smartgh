package it.unibo.pps.smartgh.model.area
import it.unibo.pps.smartgh.model.area.AreaComponentsState.AreaComponentsStateImpl
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaStatus
import it.unibo.pps.smartgh.model.area.AreaModelModule.AreaStatus.{ALARM, NORMAL}
import it.unibo.pps.smartgh.model.area.ManageSensor.ManageSensorImpl
import it.unibo.pps.smartgh.model.sensor.{AirHumiditySensor, LuminositySensor, Sensor, SensorStatus, SensorWithTimer, SoilHumiditySensor, TemperatureSensor}
import monix.execution.Ack.Continue
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject

/** Implementation of the [[ManageSensor]]. */
object ManageSensor:
  /** Apply method for [[ManageSensorImpl]].
    * @param name
    *   of the sensor
    * @param min
    *   value
    * @param max
    *   value
    * @param um
    *   the unit of measurement of the value detected by the sensor
    * @param sensor
    *   instance of the corresponding [[Sensor]]
    * @param actualVal
    *   current value registered by the sensor
    * @param message
    *   message when the sensor is in alarm state
    * @param status
    *   current value registered by the sensor
    *  @return instance of [[ManageSensorImpl]]
    */
  def apply(name: String,
            min: Double,
            max: Double,
            um: String,
            sensor: Sensor,
            actualVal: Double,
            message: String,
            status: SensorStatus): ManageSensorImpl =
    ManageSensorImpl(name, min, max, um, sensor, actualVal, message, status)


  /** Class for manage a sensor.
    * @param name
    *   of the sensor
    * @param min
    *   value
    * @param max
    *   value
    * @param um
    *   the unit of measurement of the value detected by the sensor
    * @param sensor
    *   instance of the corresponding [[Sensor]]
    * @param actualVal
    *   current value registered by the sensor
    * @param message
    *   message when the sensor is in alarm state
    * @param status
    *   current value registered by the sensor
    */
  case class ManageSensorImpl(
      name: String,
      min: Double,
      max: Double,
      um: String,
      sensor: Sensor,
      var actualVal: Double,
      message: String,
      var status: SensorStatus
  )
