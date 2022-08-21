package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaSoilMoistureControllerModule
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaSoilMoistureControllerModule.AreaSoilMoistureController
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.{
  AbstractAreaParametersView,
  AreaParametersView
}
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.GridPane

/** Object that encloses the view module for the area soil humidity parameter. */
object AreaSoilMoistureViewModule:

  /** Trait that represents the area soil moisture parameter view. */
  trait AreaSoilMoistureView extends ViewComponent[GridPane] with AreaParametersView

  /** Trait that represents the provider of the area soil moisture parameter. */
  trait Provider:

    /** The view of area soil moisture parameter. */
    val parameterView: AreaParametersView

  /** The view requirements. */
  type Requirements = AreaSoilMoistureControllerModule.Provider

  /** Trait that represents the components of the view for the area soil moisture parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaSoilMoistureView]] implementation. */
    class AreaSoilMoistureViewImpl()
        extends AbstractAreaParametersView[GridPane]("area_soil_moisture.fxml", "Soil moisture")
        with AreaSoilMoistureView:

      override val component: GridPane = loader.load[GridPane]
      private val areaSoilMoistureController = parameterController.asInstanceOf[AreaSoilMoistureController]

      @FXML
      var movingSoilBtn: Button = _

      @FXML
      var wateringBtn: Button = _

      movingSoilBtn.setOnMouseClicked(_ => areaSoilMoistureController.movingSoil())
      wateringBtn.setOnMouseClicked(_ => areaSoilMoistureController.watering())

  /** Trait that encloses the view for area soil humidity parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>
