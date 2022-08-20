package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.mvc.component.PlantSelectorMVC.PlantSelectorMVCImpl
import it.unibo.pps.smartgh.mvc.component.LoadingPlantMVC.LoadingPlantMVCImpl
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.LoadingPlantMVC
import javafx.stage.Stage
import org.junit.jupiter.api.{Test, TestInstance}
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import org.testfx.api.FxAssert.verifyThat
import org.testfx.matcher.base.NodeMatchers.{hasChildren, isVisible}
import org.testfx.matcher.control.LabeledMatchers
import org.testfx.matcher.control.LabeledMatchers.hasText
import org.testfx.framework.junit5.{ApplicationExtension, Start}

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(Array(classOf[ApplicationExtension]))
class LoadingPlantViewModuleTest extends AbstractViewTest:

  private val progressIndicatorId = "#progressIndicator"
  private val textLabelID = "#textLabel"
  private var mvc: LoadingPlantMVCImpl = _

  @Start
  private def start(stage: Stage): Unit =
    val baseView: BaseView = BaseView(appTitle, appSubtitle)
    simulationMVC = SimulationMVC(stage)
    val plantSelectorModel = PlantSelectorMVCImpl(simulationMVC, baseView).plantSelectorModel
    mvc = LoadingPlantMVC(simulationMVC, plantSelectorModel, baseView)
    startApplication(stage, baseView, mvc.loadingPlantView)

  @Test def testLabel(robot: FxRobot): Unit =
    val text = "Loading of plant data in progress, wait a few moments"

    verifyThat(textLabelID, isVisible)
    verifyThat(textLabelID, hasText(text))

  @Test def testProgressIndicator(robot: FxRobot): Unit =
    verifyThat(progressIndicatorId, isVisible)