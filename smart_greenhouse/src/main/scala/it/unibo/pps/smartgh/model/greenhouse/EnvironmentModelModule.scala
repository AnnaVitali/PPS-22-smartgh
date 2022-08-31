package it.unibo.pps.smartgh.model.greenhouse

import monix.execution.Scheduler.Implicits.global
import monix.execution.{Ack, Cancelable}
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject

/** Object that encloses the model module to manage ambient environment values and simulation time. */
object EnvironmentModelModule:

  /** A trait that represents the model for environment values and time management. */
  trait EnvironmentModel:

    /** Data structure that will contains the city's environment values. */
    type EnvironmentValues = Map[String, Any]

    /** @return
      *   environment values updated in the last hour, according to the simulation time value. Output example: HashMap(uv
      * -> 1.0, temp_c -> 25.1, lux -> 0, time -> 2022-08-06 00:00, condition -> Clear, humidity -> 69)
      */
    var currentEnvironmentValues: EnvironmentValues

    /** Method to notify the model to update main current environment values.
      * @param hour
      *   current simulation time value, expressed in hours.
      */
    def updateCurrentEnvironmentValues(hour: Int): Unit

    /** set subject for Temperature sensor [[it.unibo.pps.smartgh.model.sensor.TemperatureSensor]]. */
    val subjectTemperature: ConcurrentSubject[Double, Double]

    /** set subject for Humidity sensor [[it.unibo.pps.smartgh.model.sensor.SoilHumiditySensor]]. */
    val subjectHumidity: ConcurrentSubject[Double, Double]

    /** set subject for Luminosity sensor [[it.unibo.pps.smartgh.model.sensor.LuminositySensor]]. */
    val subjectLuminosity: ConcurrentSubject[Double, Double]

    /** set subject for soil moisture sensor [[it.unibo.pps.smartgh.model.sensor.SoilHumiditySensor]]. */
    val subjectSoilMoisture: ConcurrentSubject[Double, Double]

  /** Trait that represents the provider of the model for environment values and time management. */
  trait Provider:
    /** The environment model. */
    val environmentModel: EnvironmentModel

  /** Trait that represents the components of the model for environment values and time management. */
  trait Component:

    /** Class that contains the [[EnvironmentModel]] implementation.
      * @param environment
      *   environment's object of the selected city.
      * */
    class EnvironmentModelImpl(val environment: Environment) extends EnvironmentModel:
      
      override var currentEnvironmentValues: EnvironmentValues = _

      override def updateCurrentEnvironmentValues(hour: Int): Unit =
        val h = if hour < 10 then "0" + hour + ":00" else hour.toString + ":00"
        val forecast: Map[String, Any] = environment.environmentValues("forecast").asInstanceOf[Map[String, Any]]
        val hours: List[Map[String, Any]] = forecast("forecastday")
          .asInstanceOf[List[Map[String, Any]]]
          .foldLeft(forecast("forecastday").asInstanceOf[List[Map[String, Any]]])((_, acc) =>
            acc("hour").asInstanceOf[List[Map[String, Any]]]
          )
        val ch = hours.find(m => m("time").asInstanceOf[String].contains(h)).getOrElse(Map.empty)
        currentEnvironmentValues = Map(
          "time" -> ch("time"),
          "temp_c" -> ch("temp_c"),
          "uv" -> ch("uv"),
          "lux" -> getLuxFromUVIndex(ch("uv").toString, ch("is_day").toString.toInt, ch("cloud").toString.toInt),
          "humidity" -> ch("humidity"),
          "precipitation" -> ch("precip_mm"),
          "condition" -> ch("condition")
            .asInstanceOf[Map[String, Any]]
            .get("text")
            .fold("Not available")(res => res.toString)
        )

      override val subjectTemperature: ConcurrentSubject[Double, Double] =
        ConcurrentSubject[Double](MulticastStrategy.publish)
      override val subjectHumidity: ConcurrentSubject[Double, Double] =
        ConcurrentSubject[Double](MulticastStrategy.publish)
      override val subjectLuminosity: ConcurrentSubject[Double, Double] =
        ConcurrentSubject[Double](MulticastStrategy.publish)
      override val subjectSoilMoisture: ConcurrentSubject[Double, Double] =
        ConcurrentSubject[Double](MulticastStrategy.publish)

      private val uvIndexToLux: Map[String, Double] =
        (1 to 12).map(i => (i.toDouble.toString, (i * 10000).toDouble)).toMap

      private def getLuxFromUVIndex(uvIndex: String, isDay: Int, cloudValue: Int): Int =
        var lux: Double = uvIndexToLux(uvIndex)
        if isDay.==(0) then lux = 0
        val cloudFactor: Double = 0.05
        lux = lux - (lux * (cloudFactor * cloudValue))
        lux.max(0).toInt

  /** Trait that encloses the model for environment values and time management. */
  trait Interface extends Provider with Component