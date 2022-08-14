package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.AreaLuminosityControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
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

  trait Interface extends Provider with Component:
    self: Requirements =>
