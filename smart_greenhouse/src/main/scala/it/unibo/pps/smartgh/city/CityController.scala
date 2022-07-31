package it.unibo.pps.smartgh.city

import it.unibo.pps.smartgh.view.component.SelectCityView

trait CityController:
  def view: SelectCityView
  def view_=(view: SelectCityView): Unit

  /** Save the selected city and change.
    * @param name
    *   name of the selected city
    * @return
    *   the saved city
    */
  def saveCity(name: String): City

  /** Retrieves all cities.
    * @return
    *   a sequences of city names
    */
  def getAllCities: Seq[String]

  /** Method for searching cities beginning with the given characters.
    * @param charSequence
    *   a sequence of characters that the name of the city begins with
    * @return
    *   a sequence of city names
    */
  def searchCities(charSequence: Seq[Char]): Seq[String]

  /** Check whether all cities contain a given city.
    * @param city
    *   the city to test
    * @return
    *   true if contain the city, false otherwise
    */
  def containCity(city: String): Boolean

/** Object that can be used to create a new instances of [[CityController]]. */
object CityController:

  /** Creates a new [[CityController]] object.
    * @return
    *   a new instance of [[CityController]]
    */
  def apply(): CityController = CityControllerImpl()

  private class CityControllerImpl extends CityController:
    override var view: SelectCityView = _
    private val citySearcher: CitiesSearcher = CitiesSearcher(System.getProperty("user.home") + "/pps/cities.pl")

    override def saveCity(name: String): City = City(name)
    override def getAllCities: Seq[String] = citySearcher.getAllCities
    override def searchCities(charSequence: Seq[Char]): Seq[String] = citySearcher.searchCities(charSequence)
    override def containCity(city: String): Boolean = citySearcher.containCity(city)
