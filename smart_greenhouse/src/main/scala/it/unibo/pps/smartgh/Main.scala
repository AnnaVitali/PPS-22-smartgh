package it.unibo.pps.smartgh

import it.unibo.pps.smartgh.model.city.UploadCities
import it.unibo.pps.smartgh.model.plants.UploadPlants
import it.unibo.pps.smartgh.mvc.SimulationMVC
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import it.unibo.pps.smartgh.view.SimulationViewModule
import scalafx.Includes.*
import it.unibo.pps.smartgh.Config

/** Main class of the application. */
object Main extends JFXApp3:

  override def start(): Unit =
    UploadCities.writePrologFile(Config.path, Config.citiesInputFile, Config.citiesOutputFile)
    UploadPlants.writePrologFile(Config.path, Config.plantsInputFile, Config.plantsOutputFile)
    SimulationMVC(PrimaryStage())
