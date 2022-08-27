package it.unibo.pps.smartgh.view.component.areaParameter

import it.unibo.pps.smartgh.controller.component.areaParameter.AreaAirHumidityControllerModule
import it.unibo.pps.smartgh.controller.component.areaParameter.AreaAirHumidityControllerModule.AreaAirHumidityController
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.areaParameter.AreaAirHumidityViewModule.VentilationText
import it.unibo.pps.smartgh.view.component.areaParameter.AreaParameterView.{
  AbstractAreaParameterView,
  AreaParameterView
}
import javafx.fxml.FXML
import javafx.scene.control.ToggleButton
import javafx.scene.layout.GridPane

/** Object that encloses the view module for the area air humidity parameter. */
object AreaAirHumidityViewModule:

  /** An enum to represent the text to be displayed of the state of the ventilation. */
  enum VentilationText(val text: String):

    /** The activation status of the ventilation. */
    case ACTIVATE extends VentilationText("Activate ventilation")

    /** The deactivation status of the ventilation. */
    case DEACTIVATE extends VentilationText("Deactivate ventilation")

  /** An enum to represent the text to be displayed of the state of the atomiser. */
  enum AtomiserText(val text: String):

    /** The activation status of the atomiser. */
    case ACTIVATE extends AtomiserText("Activate atomisation")

    /** The deactivation status of the atomiser. */
    case DEACTIVATE extends AtomiserText("Deactivate atomisation")

  /** Trait that represents the area air humidity parameter view. */
  trait AreaAirHumidityView extends AreaParameterView

  /** Trait that represents the provider of the area air humidity parameter. */
  trait Provider:

    /** The view of area air humidity parameter. */
    val parameterView: AreaParameterView

  /** The view requirements. */
  type Requirements = AreaAirHumidityControllerModule.Provider

  /** Trait that represents the components of the view for the area air humidity parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaAirHumidityView]] implementation. */
    class AreaAirHumidityViewImpl()
        extends AbstractAreaParameterView("area_air_humidity.fxml", "Air humidity")
        with AreaAirHumidityView:

      override val component: GridPane = loader.load[GridPane]
      private val areaAirHumidityController = parameterController.asInstanceOf[AreaAirHumidityController]

      //noinspection VarCouldBeVal
      @FXML
      var ventilationBtn: ToggleButton = _

      //noinspection VarCouldBeVal
      @FXML
      var atomiserBtn: ToggleButton = _

      ventilationBtn.setSelected(areaAirHumidityController.isVentilationActivated)
      atomiserBtn.setSelected(areaAirHumidityController.isAtomiserActivated)

      ventilationBtn.setOnMouseClicked { _ =>
        if ventilationBtn.isSelected then
          if areaAirHumidityController.isAtomiserActivated then deactivateAtomiser()
          ventilationBtn.setText(VentilationText.DEACTIVATE.text)
          areaAirHumidityController.activateVentilation()
        else deactivateVentilation()
      }

      atomiserBtn.setOnMouseClicked { _ =>
        if atomiserBtn.isSelected then
          if areaAirHumidityController.isVentilationActivated then deactivateVentilation()
          atomiserBtn.setText(AtomiserText.DEACTIVATE.text)
          areaAirHumidityController.atomiseArea()
        else deactivateAtomiser()
      }

      private def deactivateVentilation(): Unit =
        ventilationBtn.setText(VentilationText.ACTIVATE.text)
        areaAirHumidityController.deactivateVentilation()

      private def deactivateAtomiser(): Unit =
        atomiserBtn.setText(AtomiserText.ACTIVATE.text)
        areaAirHumidityController.disableAtomiseArea()

  /** Trait that encloses the view for area air humidity parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>
