package it.unibo.pps.smartgh.model.area
import it.unibo.pps.smartgh.model.sensor.Sensor

/** Implementation of the [[ManageSensor]]. */
object ManageSensor:
  /**Class for manage a sensor. */
  case class ManageSensorImpl (name: String, min: Double, max: Double, sensor: Sensor, var actualVal: Double) //change sensorType
