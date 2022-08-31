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
import org.scalactic.TripleEquals.convertToEqualizer

/** Object that encloses the view module for the area air humidity parameter. */
object AreaAirHumidityViewModule:

  /** An enum to represent the text to be displayed of the state of the ventilation. */
  enum VentilationText(val text: String, val status: Boolean):

    /** The activation status of the ventilation. */
    case ACTIVATE extends VentilationText("Activate ventilation", false)

    /** The deactivation status of the ventilation. */
    case DEACTIVATE extends VentilationText("Deactivate ventilation", true)

  /** An enum to represent the text to be displayed of the state of the atomiser. */
  enum AtomiserText(val text: String, val status: Boolean):

    /** The activation status of the atomiser. */
    case ACTIVATE extends AtomiserText("Activate atomisation", false)

    /** The deactivation status of the atomiser. */
    case DEACTIVATE extends AtomiserText("Deactivate atomisation", true)

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

      private val areaAirHumidityController = parameterController.asInstanceOf[AreaAirHumidityController]

      //noinspection VarCouldBeVal
      @FXML
      var ventilationBtn: ToggleButton = _

      //noinspection VarCouldBeVal
      @FXML
      var atomiserBtn: ToggleButton = _

      ventilationBtn.setSelected(areaAirHumidityController.isVentilationActivated)
      setVentilationText(areaAirHumidityController.isVentilationActivated)
      atomiserBtn.setSelected(areaAirHumidityController.isAtomiserActivated)
      setAtomiserText(areaAirHumidityController.isAtomiserActivated)

      ventilationBtn.setOnMouseClicked { _ =>
        if ventilationBtn.isSelected && areaAirHumidityController.isAtomiserActivated then handleAtomiserStatus(false)
        handleVentilationStatus(ventilationBtn.isSelected)
      }

      atomiserBtn.setOnMouseClicked { _ =>
        if atomiserBtn.isSelected && areaAirHumidityController.isVentilationActivated then
          handleVentilationStatus(false)
        handleAtomiserStatus(atomiserBtn.isSelected)
      }

      private def setVentilationText(active: Boolean): Unit =
        ventilationBtn.setText(VentilationText.values.find(_.status === active).fold("")(_.text))

      private def setAtomiserText(active: Boolean): Unit =
        atomiserBtn.setText(AtomiserText.values.find(_.status === active).fold("")(_.text))

      private def handleVentilationStatus(active: Boolean): Unit =
        if active then areaAirHumidityController.activateVentilation()
        else areaAirHumidityController.deactivateVentilation()
        setVentilationText(active)

      private def handleAtomiserStatus(active: Boolean): Unit =
        if active then areaAirHumidityController.atomiseArea() else areaAirHumidityController.disableAtomiseArea()
        setAtomiserText(active)

      private def notifyStatus(active: Boolean)(thenFunc: () => Unit)(elseFunc: () => Unit) =
        if active then thenFunc else elseFunc

  /** Trait that encloses the view for area air humidity parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>
