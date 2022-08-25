package it.unibo.pps.smartgh

import it.unibo.pps.smartgh.Config
import it.unibo.pps.smartgh.model.city.UploadCities
import it.unibo.pps.smartgh.model.plants.UploadPlants
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.SelectCityMVC
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage

/** Main class of the application. */
object Main extends JFXApp3:

  override def start(): Unit =
    UploadCities.writePrologFile(Config.Path, Config.CitiesInputFile, Config.CitiesOutputFile)
    UploadPlants.writePrologFile(Config.Path, Config.PlantsInputFile, Config.PlantsOutputFile)
    val simulationMVC = SimulationMVC(PrimaryStage())
    simulationMVC.simulationView.start(SelectCityMVC(simulationMVC).selectCityView)
