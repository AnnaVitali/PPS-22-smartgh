package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaLuminosityControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.control.ToggleButton
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
      var openShieldingBtn: ToggleButton = _

      @FXML
      var closeShieldingBtn: ToggleButton = _

      openShieldingBtn.setOnMouseClicked(_ => closeShieldingBtn.setSelected(false))
      closeShieldingBtn.setOnMouseClicked(_ => openShieldingBtn.setSelected(false))

  trait Interface extends Provider with Component:
    self: Requirements =>
