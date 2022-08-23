package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.model.greenhouse.Environment
import it.unibo.pps.smartgh.model.greenhouse.EnvironmentModelModule.EnvironmentModel
import it.unibo.pps.smartgh.model.plants.Plant
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.AreaDetailsMVC.AreaDetailsMVCImpl
import it.unibo.pps.smartgh.mvc.component.{AreaDetailsMVC, AreaMVC, EnvironmentMVC, GreenHouseDivisionMVC}
import javafx.stage.Stage
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.{ApplicationExtension, Start}

/** an abstract class that is designed to instantiate elements required for testing the area details view components. */
@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class AbstractAreaDetailsViewTest extends AbstractViewTest:

  protected var environmentModel: EnvironmentModel = _
  protected var areaDetailsMVC: AreaDetailsMVCImpl = _
  protected val environment: Environment = Environment("Rome")
  protected val plant: Plant = Plant("lemon", "citrus limon")

  @Start
  def start(stage: Stage): Unit =
    val baseView = BaseView(appTitle, appSubtitle)
    simulationMVC = SimulationMVC(stage)
    simulationMVC.simulationController.startSimulationTimer()
    simulationMVC.simulationController.plantsSelected = List(plant)
    val environmentMVC = EnvironmentMVC(simulationMVC, baseView)
    environmentModel = environmentMVC.environmentModel
    simulationMVC.simulationController.environment = environment
    val greenHouseMVC = GreenHouseDivisionMVC(List(plant), simulationMVC)
    val areaMCV = AreaMVC(plant, simulationMVC, greenHouseMVC)
    areaDetailsMVC = AreaDetailsMVC(simulationMVC, baseView, areaMCV.areaModel)
    startApplication(stage, baseView, areaDetailsMVC.areaDetailsView)
