package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaAirHumidityControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
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

  trait Interface extends Provider with Component:
    self: Requirements =>
