package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaSoilMoistureControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.scene.layout.GridPane

object AreaSoilMoistureViewModule:

  trait AreaSoilMoistureView extends ViewComponent[GridPane]

  trait Provider:
    val areaSoilMoistureView: AreaSoilMoistureView

  type Requirements = AreaSoilMoistureControllerModule.Provider

  trait Component:
    context: Requirements =>

    class AreaSoilMoistureViewImpl()
        extends AbstractViewComponent[GridPane]("area_soil_moisture.fxml")
        with AreaSoilMoistureView:

      override val component: GridPane = loader.load[GridPane]

  trait Interface extends Provider with Component:
    self: Requirements =>
