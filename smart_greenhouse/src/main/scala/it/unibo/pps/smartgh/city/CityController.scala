package it.unibo.pps.smartgh.city

import it.unibo.pps.smartgh.view.component.SelectCityView

trait CityController:
  def view: SelectCityView
  def view_=(view: SelectCityView): Unit
  /**
   * save the selected city and change
   * @param name name of the selected city
   * @return the saved city
   * */
  def saveCity(name: String): City


object CityController:
  def apply(): CityController = CityControllerImpl()

  private class CityControllerImpl extends CityController:
    var _view:SelectCityView = null

    override def view: SelectCityView = _view
    override def view_=(view: SelectCityView): Unit = _view = view

    override def saveCity(name: String): City = City(name)
    



