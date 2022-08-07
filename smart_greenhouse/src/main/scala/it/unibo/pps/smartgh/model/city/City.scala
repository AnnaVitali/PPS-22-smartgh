package it.unibo.pps.smartgh.model.city

import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import requests.Response

/** This trait exposes methods for managing the selected city, represents its model. */
trait City:

  /** Data structure that will contains the city's environment values. */
  type EnvironmentValues = Map[String, Any]

  /** @return city's name. */
  def name: String

  /** @return environment values that refer to the whole day. */
  def environmentValues: EnvironmentValues

  /** @return
    *   environment values updated in the last hour, according to the simulation time value. Output example: HashMap(uv
    * -> 1.0, temp_c -> 25.1, lux -> 0, time -> 2022-08-06 00:00, condition -> Clear, humidity -> 69)
    */
  var currentEnvironmentValues: EnvironmentValues

  /** Method to notify the model to update main current environment values.
    * @param hour
    *   current simulation time value, expressed in hours.
    * @return
    *   main environment values updated
    */
  def updateCurrentEnvironmentValues(hour: Int): Unit

/** Object that can be use for managing the selected city, represents its model. */
object City:

  /** Apply method for the [[City]].
    * @return
    *   the [[City]] object.
    */
  def apply(name: String): City = CityImpl(name)

  private class CityImpl(override val name: String) extends City:

    override def environmentValues: EnvironmentValues = getEnvironmentValues
    override var currentEnvironmentValues: EnvironmentValues = _

    override def updateCurrentEnvironmentValues(hour: Int): Unit =
      val h = if hour < 10 then "0" + hour + ":00" else hour.toString + ":00"
      val forecast: Map[String, Any] = environmentValues("forecast").asInstanceOf[Map[String, Any]]
      val hours: List[Map[String, Any]] = forecast("forecastday")
        .asInstanceOf[List[Map[String, Any]]]
        .foldLeft(forecast("forecastday").asInstanceOf[List[Map[String, Any]]])((m, acc) =>
          acc("hour").asInstanceOf[List[Map[String, Any]]]
        )
      val ch = hours.find(m => m("time").asInstanceOf[String].contains(h)).getOrElse(Map.empty)
      currentEnvironmentValues = Map(
        "time" -> ch("time"),
        "temp_c" -> ch("temp_c"),
        "uv" -> ch("uv"),
        "lux" -> getLuxFromUVIndex(ch("uv").toString, ch("is_day").toString.toInt, ch("cloud").toString.toInt),
        "humidity" -> ch("humidity"),
        "condition" -> ch("condition")
          .asInstanceOf[Map[String, Any]]
          .get("text")
          .fold("Not available")(res => res.toString)
      )

    private def getEnvironmentValues: EnvironmentValues =
      val apiKey = "b619d3592d8b426e8cc92336220107"
      val query =
        "http://api.weatherapi.com/v1/forecast.json?key=" + apiKey + "&q=" + name.replace(
          " ",
          "%20"
        ) + "&days=1&aqi=no&alerts=no"
      val r: Response = requests.get(query)
      if r.statusCode == 200 then
        implicit val formats = org.json4s.DefaultFormats
        parse(r.text()).extract[EnvironmentValues]
      else Map()

    private val uvIndexToLux: Map[String, Double] =
      (1 to 12).map(i => (i.toDouble.toString, (i * 10000).toDouble)).toMap

    private def getLuxFromUVIndex(uvIndex: String, isDay: Int, cloudValue: Int): Int =
      var lux: Double = uvIndexToLux(uvIndex)
      if isDay.==(0) then lux = 0
      val cloudFactor: Double = 0.05
      lux = lux - (lux * (cloudFactor * cloudValue))
      lux.max(0).toInt
