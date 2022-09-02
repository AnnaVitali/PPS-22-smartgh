package it.unibo.pps.smartgh.view.component.areaParameter

import it.unibo.pps.smartgh.controller.component.areaParameter.AreaLuminosityControllerModule
import it.unibo.pps.smartgh.controller.component.areaParameter.AreaLuminosityControllerModule.AreaLuminosityController
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.areaParameter.AreaParameterView.{
  AbstractAreaParameterView,
  AreaParameterView
}
import javafx.fxml.FXML
import javafx.scene.control.{Label, Slider, ToggleButton}
import javafx.scene.layout.GridPane
import scalafx.util.StringConverter
import scalafx.util.StringConverter.sfxStringConverter2jfx

/** Object that encloses the view module for the area luminosity parameter. */
object AreaLuminosityViewModule:

  private val LampFactor: Double = 1000.0

  /** Trait that represents the area luminosity parameter view. */
  trait AreaLuminosityView extends AreaParameterView:

    /** Set up actions based on the opening of the area gates.
      * @param isGatesOpen
      *   the area gates state, true if is opened, false otherwise.
      */
    def setUpActions(isGatesOpen: Boolean): Unit

  /** Trait that represents the provider of the area luminosity parameter. */
  trait Provider:

    /** The view of area luminosity parameter. */
    val parameterView: AreaParameterView

  /** The view requirements. */
  type Requirements = AreaLuminosityControllerModule.Provider

  /** Trait that represents the components of the view for the area luminosity parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaLuminosityView]] implementation. */
    class AreaLuminosityViewImpl()
        extends AbstractAreaParameterView("area_luminosity.fxml", "Luminosity")
        with AreaLuminosityView:

      private val areaLuminosityController = parameterController.asInstanceOf[AreaLuminosityController]

      //noinspection VarCouldBeVal
      @FXML
      protected var upShieldingBtn: ToggleButton = _

      //noinspection VarCouldBeVal
      @FXML
      protected var downShieldingBtn: ToggleButton = _

      //noinspection VarCouldBeVal
      @FXML
      protected var lampBrightnessSlider: Slider = _

      lampBrightnessSlider.setValue(areaLuminosityController.getLampValue / LampFactor)
      initShieldingBtn(areaLuminosityController.isShielded)

      upShieldingBtn.setOnMouseClicked { _ =>
        if upShieldingBtn.isSelected then areaLuminosityController.shieldsUp()
        else upShieldingBtn.setSelected(true)
      }

      downShieldingBtn.setOnMouseClicked { _ =>
        if downShieldingBtn.isSelected then areaLuminosityController.shieldsDown()
        else downShieldingBtn.setSelected(true)
      }

      lampBrightnessSlider.setOnMouseReleased(_ =>
        areaLuminosityController.updLampValue(lampBrightnessSlider.getValue * LampFactor)
      )

      lampBrightnessSlider.setLabelFormatter(
        sfxStringConverter2jfx(StringConverter(_.dropRight(1).toDouble, _.toString + "k"))
      )

      override def setUpActions(isGatesOpen: Boolean): Unit =
        if isGatesOpen then
          disablingShieldingBtn(true)
          if downShieldingBtn.isSelected then
            initShieldingBtn(false)
            areaLuminosityController.shieldsUp()
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
