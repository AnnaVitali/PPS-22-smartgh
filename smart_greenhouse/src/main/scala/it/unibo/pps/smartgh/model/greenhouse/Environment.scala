package it.unibo.pps.smartgh.model.greenhouse

import com.sun.net.httpserver.Authenticator.Success
import monix.eval.Task
import monix.execution.Ack
import monix.execution.Ack.Continue
import monix.execution.Scheduler.Implicits.global
import monix.reactive.MulticastStrategy
import monix.reactive.subjects.ConcurrentSubject
import org.json4s.*
import org.json4s.jackson.JsonMethods.*
import requests.Response

import scala.concurrent.Future

/** This trait represents the city's environment. */
trait Environment:

  /** Data structure that will contains the city's environment values. */
  type EnvironmentValues = Map[String, Any]

  /** @return city's name. */
  def nameCity: String

  /** @return city's latitude. */
  def latitude: String

  /** @return city's longitude. */
  def longitude: String

  /** @return environment values that refer to the whole day. */
  var environmentValues: EnvironmentValues

/** Object that represents the city's environment. */
object Environment:

  /** Apply method for the [[Environment]].
    * @param name
    *   city's name
    * @param latitude
    *   city's latitude
    * @param longitude
    *   city's longitude
    * @return
    *   the [[Environment]] object.
    */
  def apply(name: String, latitude: String, longitude: String): Environment = EnvironmentImpl(name, latitude, longitude)

  /** Class that contains the [[Environment]] implementation.
    * @param nameCity
    *   city's name.
    * @param latitude
    *   city's latitude
    * @param longitude
    *   city's longitude
    */
  private class EnvironmentImpl(override val nameCity: String,
                                override val latitude: String,
                                override val longitude: String) extends Environment:

    override var environmentValues: EnvironmentValues = _
    private val subjectEnvironmentValues: ConcurrentSubject[EnvironmentValues, EnvironmentValues] =
      ConcurrentSubject[EnvironmentValues](MulticastStrategy.publish)
    private val onNextEnvironmentValuesEmitted: EnvironmentValues => Future[Ack] =
      v => {
        environmentValues = v
        Continue
      }
    subjectEnvironmentValues.subscribe(
      onNextEnvironmentValuesEmitted,
      (ex: Throwable) => ex.printStackTrace(),
      () => {}
    )
    getEnvironmentValues

    private def getEnvironmentValues =
      import scala.util.{Failure, Success, Try}
      import java.text.Normalizer
      Task {
        val apiKey = "b619d3592d8b426e8cc92336220107"
        val query =
          s"http://api.weatherapi.com/v1/forecast.json?key=${apiKey}&q=${latitude},${longitude}&days=1&aqi=no&alerts=no"
        Try (requests.get(query)) match
          case Success (r: Response) =>
            implicit val formats: DefaultFormats.type = org.json4s.DefaultFormats
            subjectEnvironmentValues.onNext(parse(r.text()).extract[EnvironmentValues])
          case Failure(_) => subjectEnvironmentValues.onNext(Map())
        subjectEnvironmentValues.onComplete()
      }.executeAsync.runToFuture