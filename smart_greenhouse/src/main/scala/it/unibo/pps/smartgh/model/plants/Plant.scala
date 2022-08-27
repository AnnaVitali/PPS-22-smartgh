package it.unibo.pps.smartgh.model.plants

import cats.syntax.eq.catsSyntaxEq
import monix.execution.Scheduler.Implicits.global
import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import requests.*

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}

/** This trait exposes methods for managing a selected plant, it represents its model. */
trait Plant:

  /** Data structure that will contains plant's optimal values. */
  type OptimalValues = Map[String, Any]

  /** @return plant's name */
  def name: String

  /** @return plant's id */
  def id: String

  /** @return plant's image url */
  def imageUrl: String

  /** @return plant's optimal values */
  def optimalValues: OptimalValues

  /** @return plant's description */
  def description: String

/** Object that can be use for managing a selected plant, it represents its model. */
object Plant:

  /** Creates a new [[Plant]] object.
    * @param name
    *   plant's name.
    * @param id
    *   plant's id
    */
  def apply(name: String, id: String): Plant = PlantImpl(name, id)

  /** Class that contains the [[Plant]] implementation.
    * @param name
    *   plant's name.
    * @param id
    *   plant's id
    */
  private class PlantImpl(override val name: String, override val id: String) extends Plant:

    type RequestResult = Map[String, Any]
    private val info: RequestResult = getInfo

    override def imageUrl: String = getImageUrl(info)

    private val detectedParameters: List[String] = List(
      "max_light_lux", "min_light_lux", "max_temp", "min_temp", "max_env_humid", "min_env_humid", "max_soil_moist",
      "min_soil_moist"
    )
    override def optimalValues: OptimalValues = getOptimalValues(info)

    override def description: String = getDescription

    private def getAccessToken: String =
      val clientID = "jQ2R0lkr7HHoOnK4woHxhHSwY3DO453BEoSe8tKZ"
      //noinspection SpellCheckingInspection
      val clientSecret =
        "ZmhY9u1uNecGqnX5tmm8t0Tqj4h8OhQD52kp5fTxiyP3Q0DnF0LqVIxPjqvidYuTqeQ1YEZkDG4sgjSm4QHmLFkEbXA7wwizI00SS2BfoDc1WL3xDDiR6l6VtdCTvgT0"
      val url = "https://open.plantbook.io/api/v1/token/"
      val data = Map("grant_type" -> "client_credentials", "client_id" -> clientID, "client_secret" -> clientSecret)
      Try(requests.post(url = url, data = data)) match {
        case Success(r: Response) =>
          implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats
          parse(r.text()).extract[RequestResult].get("access_token").fold[String]("")(res => res.toString)
        case Failure(_) => ""
      }

    private def getInfo: RequestResult =
      val accessToken = getAccessToken
      val authorizationHeader = ("Authorization", "Bearer " + accessToken)
      val url =
        "https://open.plantbook.io/api/v1/plant/detail/" + id.replace(" ", "%20").replace("\'", "") + "/?format=json"
      Try(requests.get(url = url, headers = Iterable(authorizationHeader))) match {
        case Success(r: Response) =>
          implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats
          parse(r.text()).extract[RequestResult]
        case Failure(_) => Map.empty
      }

    private def getImageUrl(info: RequestResult): String =
      info.get("image_url").fold[String]("images/plantIcon.png")(res => res.toString)

    private def getOptimalValues(info: RequestResult): RequestResult =
      info.filter((f, _) => detectedParameters contains f)

    private def getDescription: String =
      val query =
        "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=" + name
          .replace(" ", "%20")
          .replace("\'", "")
      try {
        val r: Response = requests.get(query)
        implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats
        parse(r.text())
          .findField((f, _) => f === "extract")
          .fold("No description available")(res => res._2.values.toString)
      } catch {
        case e: RequestFailedException => "No description available"
      }
