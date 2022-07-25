package it.unibo.pps.smartgh

import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.{BaseView, SelectCityView}
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.layout.Pane
import scalafx.scene.Scene

import java.io.IOException
import scalafx.Includes.*

import scala.util.Random

object Main extends JFXApp3:

  override def start(): Unit =
    val s = SimulationView(PrimaryStage())
