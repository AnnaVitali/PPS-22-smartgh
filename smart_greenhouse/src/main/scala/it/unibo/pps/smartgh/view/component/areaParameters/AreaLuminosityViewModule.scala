package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaLuminosityControllerModule
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaLuminosityControllerModule.AreaLuminosityController
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.{
  AbstractAreaParametersView,
  AreaParametersView
}
import javafx.util.StringConverter
import javafx.fxml.FXML
import javafx.scene.control.{Label, Slider, ToggleButton}
import javafx.scene.layout.GridPane

object AreaLuminosityViewModule:

  private val lampFactor: Double = 1000.0

  trait AreaLuminosityView extends ViewComponent[GridPane] with AreaParametersView:
    def checkGatesState(isOpen: Boolean): Unit

  trait Provider:
    val parameterView: AreaParametersView

  type Requirements = AreaLuminosityControllerModule.Provider

  trait Component:
    context: Requirements =>

    class AreaLuminosityViewImpl()
        extends AbstractAreaParametersView[GridPane]("area_luminosity.fxml", "Luminosity")
        with AreaLuminosityView:

      override val component: GridPane = loader.load[GridPane]
      private val areaLuminosityController = parameterController.asInstanceOf[AreaLuminosityController]

      @FXML
      var upShieldingBtn: ToggleButton = _

      @FXML
      var downShieldingBtn: ToggleButton = _

      @FXML
      var lampBrightnessSlider: Slider = _

      lampBrightnessSlider.setValue(areaLuminosityController.getLampValue / lampFactor)
      initShieldingBtn(areaLuminosityController.isShielded)

      upShieldingBtn.setOnMouseClicked { _ =>
        downShieldingBtn.setSelected(false)
        areaLuminosityController.shieldsUp()
      }

      downShieldingBtn.setOnMouseClicked { _ =>
        upShieldingBtn.setSelected(false)
        areaLuminosityController.shieldsDown()
      }

      lampBrightnessSlider.setOnMouseReleased(_ =>
        areaLuminosityController.updLampValue(lampBrightnessSlider.getValue * lampFactor)
      )

      lampBrightnessSlider.setLabelFormatter(new StringConverter[java.lang.Double]:
        override def toString(n: java.lang.Double): String = n + "k"
        override def fromString(s: String): java.lang.Double = java.lang.Double.valueOf(s.dropRight(1))
      )

      override def checkGatesState(isOpen: Boolean): Unit =
        if isOpen then
          disablingShieldingBtn(true)
          if downShieldingBtn.isSelected then initShieldingBtn(false)
        else if downShieldingBtn.isDisabled then disablingShieldingBtn(false)

      private def initShieldingBtn(isShielded: Boolean): Unit =
        downShieldingBtn.setSelected(isShielded)
        upShieldingBtn.setSelected(!isShielded)

      private def disablingShieldingBtn(disabled: Boolean): Unit =
        downShieldingBtn.setDisable(disabled)
        upShieldingBtn.setDisable(disabled)

  trait Interface extends Provider with Component:
    self: Requirements =>
