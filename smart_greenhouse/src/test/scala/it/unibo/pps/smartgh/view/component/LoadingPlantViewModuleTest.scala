package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.LoadingPlantMVC
import it.unibo.pps.smartgh.mvc.component.LoadingPlantMVC
import it.unibo.pps.smartgh.mvc.component.PlantSelectorMVC
import javafx.stage.Stage
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.{Test, TestInstance}
import org.testfx.api.FxAssert.verifyThat
import org.testfx.api.FxRobot
import org.testfx.framework.junit5.{ApplicationExtension, Start}
import org.testfx.matcher.base.NodeMatchers.{hasChildren, isVisible}
import org.testfx.matcher.control.LabeledMatchers
import org.testfx.matcher.control.LabeledMatchers.hasText

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class LoadingPlantViewModuleTest extends AbstractViewTest:

  private val progressIndicatorId = "#progressIndicator"
  private val textLabelID = "#textLabel"
  private var mvc: LoadingPlantMVC = _

  @Start
  private def start(stage: Stage): Unit =
    val simulationMVC = SimulationMVC(stage)
    val plantSelectorModel = PlantSelectorMVC(simulationMVC).plantSelectorModel
    mvc = LoadingPlantMVC(simulationMVC, plantSelectorModel)
    simulationMVC.simulationView.start(mvc.loadingPlantView)

  @Test def testLabel(robot: FxRobot): Unit =
    val text = "Loading of plant data in progress, wait a few moments"

    verifyThat(textLabelID, isVisible)
    verifyThat(textLabelID, hasText(text))

  @Test def testProgressIndicator(robot: FxRobot): Unit =
    verifyThat(progressIndicatorId, isVisible)
