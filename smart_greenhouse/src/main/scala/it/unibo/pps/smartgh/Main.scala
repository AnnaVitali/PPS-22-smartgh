package it.unibo.pps.smartgh

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import it.unibo.pps.smartgh.view.SimulationView

import scalafx.Includes.*

/** Main class of the application. */
object Main extends JFXApp3:

  override def start(): Unit =
    val s = SimulationView(PrimaryStage())
