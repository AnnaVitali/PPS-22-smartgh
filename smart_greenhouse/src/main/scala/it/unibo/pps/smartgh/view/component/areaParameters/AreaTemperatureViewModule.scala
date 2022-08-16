package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaTemperatureControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
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

  trait Interface extends Provider with Component:
    self: Requirements =>
