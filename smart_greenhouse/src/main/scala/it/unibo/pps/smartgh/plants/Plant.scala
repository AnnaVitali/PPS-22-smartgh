package it.unibo.pps.smartgh.plants

import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import requests.*

trait Plant:

  type OptimalValues = Map[String, Any]

  /** @return plant's name */
  def name: String

  /** @return plant's id */
  def id: String

  /** @return plant's image url */
  def imageUrl: String

  /** @return plant's optimal values */
  def optimalValues : OptimalValues

  /** @return plant's description */
  def description : String

object Plant:
  def apply(name: String, id: String): Plant = PlantImpl(name, id)

  private class PlantImpl(override val name: String, override val id: String) extends Plant:

    // todo: gestire casi in cui non esiste il campo

    type RequestResult = Map[String,Any]
    private val info: RequestResult = getInfo()

    override def imageUrl: String = getImageUrl(info)

    private val detectedParameters: List[String] = List(
      "max_light_lux",
      "min_light_lux",
      "max_temp",
      "min_temp",
      "max_env_humid",
      "min_env_humid",
      "max_soil_moist",
      "min_soil_moist"
    )
    override def optimalValues: OptimalValues = getOptimalValues(info)
    override def description : String = getDescription()

    private def getInfo() : RequestResult =
      val accessToken = "vXzbza21SvZMORwLlKY5gS8cNb0XE4"
      val authorizationHeader = ("Authorization", "Bearer " + accessToken)
      val query = "https://open.plantbook.io/api/v1/plant/detail/" + id.replace(" ", "%20") + "/?format=json"
      val r : Response = requests.get(url=query, headers = Iterable(authorizationHeader))
      if r.statusCode == 200 then
        implicit val formats = org.json4s.DefaultFormats
        parse(r.text()).extract[RequestResult]
      else Map()

    private def getImageUrl(info : RequestResult) : String =
      info.get("image_url").get.toString

    private def getOptimalValues(info : RequestResult) : RequestResult =
      info.filter((f, v) => detectedParameters contains f)

    private def getDescription(): String =
      val query = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=" + id.replace(" ", "%20")
      val r: Response = requests.get(query)
      if r.statusCode == 200 then
        implicit val formats = org.json4s.DefaultFormats
        parse(r.text()).findField((f,v) => f == "extract").get._2.values.toString
      else ""