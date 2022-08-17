package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaLuminosityControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.control.{Slider, ToggleButton}
import javafx.scene.layout.GridPane

object AreaLuminosityViewModule:

  trait AreaLuminosityView extends ViewComponent[GridPane]

  trait Provider:
    val areaLuminosityView: AreaLuminosityView

  type Requirements = AreaLuminosityControllerModule.Provider

  trait Component:
    context: Requirements =>

    class AreaLuminosityViewImpl()
        extends AbstractViewComponent[GridPane]("area_luminosity.fxml")
        with AreaLuminosityView:

      override val component: GridPane = loader.load[GridPane]

      @FXML
      var upShieldingBtn: ToggleButton = _

      @FXML
      var downShieldingBtn: ToggleButton = _

      @FXML
      var lampBrightnessSlider: Slider = _

      upShieldingBtn.setOnMouseClicked { _ =>
        downShieldingBtn.setSelected(false)
        areaLuminosityController.shieldsDown()
      }

      downShieldingBtn.setOnMouseClicked { _ =>
        upShieldingBtn.setSelected(false)
        areaLuminosityController.shieldsUp()
      }

      lampBrightnessSlider.setOnMouseReleased(_ => areaLuminosityController.updLampValue(lampBrightnessSlider.getValue))

  trait Interface extends Provider with Component:
    self: Requirements =>
