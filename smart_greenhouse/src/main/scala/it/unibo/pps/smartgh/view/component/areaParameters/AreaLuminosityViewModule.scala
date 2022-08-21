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

/** Object that encloses the view module for the area luminosity parameter. */
object AreaLuminosityViewModule:

  private val lampFactor: Double = 1000.0

  /** Trait that represents the area luminosity parameter view. */
  trait AreaLuminosityView extends ViewComponent[GridPane] with AreaParametersView:

    /** Set up actions based on the opening of the area gates.
      * @param isGatesOpen
      *   the area gates state, true if is opened, false otherwise.
      */
    def setUpActions(isGatesOpen: Boolean): Unit

  /** Trait that represents the provider of the area luminosity parameter. */
  trait Provider:

    /** The view of area luminosity parameter. */
    val parameterView: AreaParametersView

  /** The view requirements. */
  type Requirements = AreaLuminosityControllerModule.Provider

  /** Trait that represents the components of the view for the area luminosity parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaLuminosityView]] implementation. */
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

      override def setUpActions(isGatesOpen: Boolean): Unit =
        if isGatesOpen then
          disablingShieldingBtn(true)
          if downShieldingBtn.isSelected then initShieldingBtn(false)
        else if downShieldingBtn.isDisabled then disablingShieldingBtn(false)

      private def initShieldingBtn(isShielded: Boolean): Unit =
        downShieldingBtn.setSelected(isShielded)
        upShieldingBtn.setSelected(!isShielded)

      private def disablingShieldingBtn(disabled: Boolean): Unit =
        downShieldingBtn.setDisable(disabled)
        upShieldingBtn.setDisable(disabled)

  /** Trait that encloses the view for area luminosity parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>
