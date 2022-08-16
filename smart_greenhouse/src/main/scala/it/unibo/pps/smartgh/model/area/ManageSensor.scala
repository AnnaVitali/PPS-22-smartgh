package it.unibo.pps.smartgh.model.area
import it.unibo.pps.smartgh.model.sensor.Sensor
import it.unibo.pps.smartgh.model.sensor.SensorStatus

/** Implementation of the [[ManageSensor]]. */
object ManageSensor:
  /** Class for manage a sensor.
    * @param name
    *   of the sensor
    * @param min
    *   value
    * @param max
    *   value
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
      sensor: Sensor,
      var actualVal: Double,
      message: String,
      var status: SensorStatus
  )
