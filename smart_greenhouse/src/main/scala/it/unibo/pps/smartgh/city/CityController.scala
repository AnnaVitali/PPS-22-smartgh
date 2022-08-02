package it.unibo.pps.smartgh.city

import it.unibo.pps.smartgh.view.component.SelectCityView
/** This trait exposes methods for managing the selected city, represents its controller. */
trait CityController:
  var view: SelectCityView
  /**Save the selected city and change
   * @param name name of the selected city
   * @return the saved city
   * */
  def saveCity(name: String): City

/** Object that can used to create a new instances of [[CityController]]. */
object CityController:
  /** Apply method for the [[CityController]].
   * @return
   *   the [[CityController]] object.
   */
  def apply(): CityController = CityControllerImpl()

  private class CityControllerImpl extends CityController:
    override var view: SelectCityView = _

    override def saveCity(name: String): City = City(name)
    



