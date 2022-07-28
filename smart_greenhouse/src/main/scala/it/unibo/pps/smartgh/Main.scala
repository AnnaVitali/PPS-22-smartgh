package it.unibo.pps.smartgh

import it.unibo.pps.smartgh.city.UploadCities
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import it.unibo.pps.smartgh.view.SimulationView
import scalafx.Includes.*

/** Main class of the application. */
object Main extends JFXApp3:

  private val path = System.getProperty("user.home") + "/pps/"

  override def start(): Unit =
    UploadCities.writePrologFile(path, "cities.txt", "cities.pl")
    val s = SimulationView(PrimaryStage())