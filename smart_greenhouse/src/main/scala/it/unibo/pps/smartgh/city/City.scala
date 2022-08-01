package it.unibo.pps.smartgh.city

import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import requests.Response

trait City:
  /** data structure that will contains the city's environment values */
  type EnvironmentValues = Map[String, Any]

  /** @return city's name */
  def name: String

  /** @return
    *   city's environment values, example of returned value: (location, HashMap(name -> Rome, country -> Italy, tz_id
    *   -> Europe/Rome, region -> Lazio, lon -> 12.48, localtime -> 2022-07-27 9:11, localtime_epoch -> 1658905896, lat
    *   -> 41.9)) (current,HashMap(temp_f -> 78.8, uv -> 7.0, vis_km -> 10.0, feelslike_c -> 26.9, gust_kph -> 13.7,
    *   pressure_in -> 29.83, precip_mm -> 0.0, wind_dir -> ESE, wind_mph -> 4.3, last_updated_epoch ->1658905200,
    *   feelslike_f -> 80.3, wind_kph -> 6.8, wind_degree -> 120, precip_in -> 0.0, gust_mph -> 8.5, vis_miles -> 6.0,
    *   temp_c -> 26.0, is_day -> 1, condition -> Map(text -> Sunny, icon ->
    *   //cdn.weatherapi.com/weather/64x64/day/113.png, code -> 1000), humidity -> 84, cloud -> 0, pressure_mb ->
    *   1010.0, last_updated -> 2022-07-27 09:00))
    */
  def environmentValues: EnvironmentValues

object City:
  def apply(name: String): City = CityImpl(name)

  private class CityImpl(override val name: String) extends City:
    private var envValues: EnvironmentValues = setEnvironmentValues()
    override def environmentValues: EnvironmentValues = envValues

    private def setEnvironmentValues(): EnvironmentValues =
      val apiKey = "b619d3592d8b426e8cc92336220107"
      val query = "http://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + name.replace(" ", "%20") + "&aqi=no"
      val r: Response = requests.get(query)
      if r.statusCode == 200 then
        implicit val formats = org.json4s.DefaultFormats
        parse(r.text()).extract[EnvironmentValues]
      else Map()
