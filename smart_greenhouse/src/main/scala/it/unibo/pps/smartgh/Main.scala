package it.unibo.pps.smartgh

import it.unibo.pps.smartgh.model.city.UploadCities
import it.unibo.pps.smartgh.model.plants.UploadPlants
import it.unibo.pps.smartgh.mvc.SimulationMVC
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import it.unibo.pps.smartgh.view.SimulationViewModule
import scalafx.Includes.*

/** Main class of the application. */
object Main extends JFXApp3:

  private val path = System.getProperty("user.home") + "/pps/"

  override def start(): Unit =
    UploadCities.writePrologFile(path, "cities.txt", "cities.pl")
    UploadPlants.writePrologFile(path, "plants.txt", "plants.pl")
    SimulationMVC(PrimaryStage())
