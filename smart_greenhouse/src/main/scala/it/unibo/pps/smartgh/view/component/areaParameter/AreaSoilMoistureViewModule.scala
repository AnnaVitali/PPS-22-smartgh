package it.unibo.pps.smartgh.view.component.areaParameter

import it.unibo.pps.smartgh.controller.component.areaParameter.AreaSoilMoistureControllerModule
import it.unibo.pps.smartgh.controller.component.areaParameter.AreaSoilMoistureControllerModule.AreaSoilMoistureController
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.areaParameter.AreaParameterView.{
  AbstractAreaParameterView,
  AreaParameterView
}
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.GridPane

/** Object that encloses the view module for the area soil humidity parameter. */
object AreaSoilMoistureViewModule:

  /** Trait that represents the area soil moisture parameter view. */
  trait AreaSoilMoistureView extends AreaParameterView

  /** Trait that represents the provider of the area soil moisture parameter. */
  trait Provider:

    /** The view of area soil moisture parameter. */
    val parameterView: AreaParameterView

  /** The view requirements. */
  type Requirements = AreaSoilMoistureControllerModule.Provider

  /** Trait that represents the components of the view for the area soil moisture parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaSoilMoistureView]] implementation. */
    class AreaSoilMoistureViewImpl()
        extends AbstractAreaParameterView("area_soil_moisture.fxml", "Soil moisture")
        with AreaSoilMoistureView:

      private val areaSoilMoistureController = parameterController.asInstanceOf[AreaSoilMoistureController]

      //noinspection VarCouldBeVal
      @FXML
      var movingSoilBtn: Button = _

      //noinspection VarCouldBeVal
      @FXML
      var wateringBtn: Button = _

      movingSoilBtn.setOnMouseClicked(_ => areaSoilMoistureController.movingSoil())
      wateringBtn.setOnMouseClicked(_ => areaSoilMoistureController.watering())

  /** Trait that encloses the view for area soil humidity parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>
