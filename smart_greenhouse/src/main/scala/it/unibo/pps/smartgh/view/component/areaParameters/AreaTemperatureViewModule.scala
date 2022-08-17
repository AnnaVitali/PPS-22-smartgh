package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaTemperatureControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.control.ToggleButton
import javafx.scene.layout.GridPane

object AreaTemperatureViewModule:

  trait AreaTemperatureView extends ViewComponent[GridPane]

  trait Provider:
    val areaTemperatureView: AreaTemperatureView

  type Requirements = AreaTemperatureControllerModule.Provider

  trait Component:
    context: Requirements =>

    class AreaTemperatureViewImpl()
        extends AbstractViewComponent[GridPane]("area_temperature.fxml")
        with AreaTemperatureView:

      override val component: GridPane = loader.load[GridPane]

      @FXML
      var openStructureBtn: ToggleButton = _

      @FXML
      var closeStructureBtn: ToggleButton = _

      openStructureBtn.setOnMouseClicked(_ => closeStructureBtn.setSelected(false))
      closeStructureBtn.setOnMouseClicked(_ => openStructureBtn.setSelected(false))

  trait Interface extends Provider with Component:
    self: Requirements =>
