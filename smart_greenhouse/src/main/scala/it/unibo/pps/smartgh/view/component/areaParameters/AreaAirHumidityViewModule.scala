package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaAirHumidityControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.control.ToggleButton
import javafx.scene.layout.GridPane

object AreaAirHumidityViewModule:

  trait AreaAirHumidityView extends ViewComponent[GridPane]

  trait Provider:
    val areaAirHumidityView: AreaAirHumidityView

  type Requirements = AreaAirHumidityControllerModule.Provider

  trait Component:
    context: Requirements =>

    class AreaAirHumidityViewImpl()
        extends AbstractViewComponent[GridPane]("area_air_humidity.fxml")
        with AreaAirHumidityView:

      override val component: GridPane = loader.load[GridPane]

      @FXML
      var ventilationBtn: ToggleButton = _

      @FXML
      var atomiserBtn: ToggleButton = _

      ventilationBtn.setOnMouseClicked { _ =>
        if ventilationBtn.isSelected then
          ventilationBtn.setText("Deactivate the ventilation")
          areaAirHumidityController.activateVentilation()
        else
          ventilationBtn.setText("Activate the ventilation")
          areaAirHumidityController.deactivateVentilation()
      }

      atomiserBtn.setOnMouseClicked { _ =>
        if atomiserBtn.isSelected then
          atomiserBtn.setText("Disable atomise area")
          areaAirHumidityController.atomiseArea()
        else
          atomiserBtn.setText("Atomise area")
          areaAirHumidityController.disableAtomiseArea()
      }

  trait Interface extends Provider with Component:
    self: Requirements =>
