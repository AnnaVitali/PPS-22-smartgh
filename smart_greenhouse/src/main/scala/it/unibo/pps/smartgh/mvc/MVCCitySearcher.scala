package it.unibo.pps.smartgh.mvc

import it.unibo.pps.smartgh.controller.CitySearcherControllerModule
import it.unibo.pps.smartgh.controller.CitySearcherControllerModule.CitySearcherController
import it.unibo.pps.smartgh.model.city.{CitySearcherModelModule, UploadCities}
import it.unibo.pps.smartgh.model.city.CitySearcherModelModule.CitySearcherModel
import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.{BaseView, CitySearcherViewModule}
import it.unibo.pps.smartgh.view.component.CitySearcherViewModule.CitySearcherView

object MVCCitySearcher:

  val fileName: String = System.getProperty("user.home") + "/pps/cities.pl"

  def apply(simulationView: SimulationView, baseView: BaseView): MVCCitySearcherImpl =
    MVCCitySearcherImpl(simulationView, baseView)

  class MVCCitySearcherImpl(simulationView: SimulationView, baseView: BaseView)
      extends CitySearcherModelModule.Interface
      with CitySearcherViewModule.Interface
      with CitySearcherControllerModule.Interface:

    override val citySearcherModel: CitySearcherModel = CitySearcherModelImpl(fileName)
    override val citySearcherController: CitySearcherController = CitySearcherControllerImpl()
    override val citySearcherView: CitySearcherView = CitySearcherViewViewImpl(simulationView, baseView)
