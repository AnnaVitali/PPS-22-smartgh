package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaAirHumidityControllerModule
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaAirHumidityControllerModule.AreaAirHumidityController
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.areaParameters.AreaAirHumidityViewModule.VentilationText
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.{
  AbstractAreaParametersView,
  AreaParametersView
}
import javafx.fxml.FXML
import javafx.scene.control.{Label, ToggleButton}
import javafx.scene.layout.{Border, GridPane}

object AreaAirHumidityViewModule:

  enum VentilationText(val text: String):
    case ACTIVE extends VentilationText("Activate the ventilation")
    case DEACTIVE extends VentilationText("Deactivate the ventilation")

  enum AtomiserText(val text: String):
    case ACTIVE extends AtomiserText("Atomise area")
    case DEACTIVE extends AtomiserText("Disable atomise area")

  trait AreaAirHumidityView extends ViewComponent[GridPane] with AreaParametersView

  trait Provider:
    val parameterView: AreaParametersView

  type Requirements = AreaAirHumidityControllerModule.Provider

  trait Component:
    context: Requirements =>

    class AreaAirHumidityViewImpl()
        extends AbstractAreaParametersView[GridPane]("area_air_humidity.fxml", "Air humidity")
        with AreaAirHumidityView:

      override val component: GridPane = loader.load[GridPane]
      private val areaAirHumidityController = parameterController.asInstanceOf[AreaAirHumidityController]

      @FXML
      var ventilationBtn: ToggleButton = _

      @FXML
      var atomiserBtn: ToggleButton = _

      ventilationBtn.setOnMouseClicked { _ =>
        if ventilationBtn.isSelected then
          ventilationBtn.setText(VentilationText.DEACTIVE.text)
          areaAirHumidityController.activateVentilation()
        else
          ventilationBtn.setText(VentilationText.ACTIVE.text)
          areaAirHumidityController.deactivateVentilation()
      }

      atomiserBtn.setOnMouseClicked { _ =>
        if atomiserBtn.isSelected then
          atomiserBtn.setText(AtomiserText.DEACTIVE.text)
          areaAirHumidityController.atomiseArea()
        else
          atomiserBtn.setText(AtomiserText.ACTIVE.text)
          areaAirHumidityController.disableAtomiseArea()
      }

  trait Interface extends Provider with Component:
    self: Requirements =>
