package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaSoilMoistureControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.{
  AbstractAreaParametersView,
  AreaParametersView
}
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.GridPane

object AreaSoilMoistureViewModule:

  trait AreaSoilMoistureView extends ViewComponent[GridPane] with AreaParametersView

  trait Provider:
    val areaSoilMoistureView: AreaSoilMoistureView

  type Requirements = AreaSoilMoistureControllerModule.Provider

  trait Component:
    context: Requirements =>

    class AreaSoilMoistureViewImpl()
        extends AbstractAreaParametersView[GridPane]("area_soil_moisture.fxml")
        with AreaSoilMoistureView:

      override val component: GridPane = loader.load[GridPane]

      @FXML
      var movingSoilBtn: Button = _

      @FXML
      var wateringBtn: Button = _

      descriptionLabel.setText("Soil moisture " + areaSoilMoistureController.getOptimalValues.toString())
      movingSoilBtn.setOnMouseClicked(_ => areaSoilMoistureController.movingSoil())
      wateringBtn.setOnMouseClicked(_ => areaSoilMoistureController.watering())

  trait Interface extends Provider with Component:
    self: Requirements =>
