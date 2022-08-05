package it.unibo.pps.smartgh.model.area
//TODO DOC
object ManageSensor:
  /**Class for manage a sensor. */
  case class ManageSensorImpl (name: String, min: Float, max: Float, sensor: Any, var actualVal: Float) //change sensorType
