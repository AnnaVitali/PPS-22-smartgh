package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaTemperatureControllerModule
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.{
  AbstractAreaParametersView,
  AreaParametersView
}
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, ToggleButton}
import javafx.scene.layout.GridPane

object AreaTemperatureViewModule:

  trait AreaTemperatureView extends ViewComponent[GridPane] with AreaParametersView

  trait Provider:
    val areaTemperatureView: AreaTemperatureView

  type Requirements = AreaTemperatureControllerModule.Provider

  trait Component:
    context: Requirements =>

    class AreaTemperatureViewImpl()
        extends AbstractAreaParametersView[GridPane]("area_temperature.fxml", "Temperature")
        with AreaTemperatureView:

      private val tempStep = 0.5
      private val tempRange = (10.0 + tempStep, 40.0 - tempStep)
      override val component: GridPane = loader.load[GridPane]

      @FXML
      var openStructureBtn: ToggleButton = _

      @FXML
      var closeStructureBtn: ToggleButton = _

      @FXML
      var regulateTempLabel: Label = _

      @FXML
      var minusTempBtn: Button = _

      @FXML
      var plusTempBtn: Button = _

      updateDescription(areaTemperatureController.getOptimalValues)

      openStructureBtn.setOnMouseClicked { _ =>
        closeStructureBtn.setSelected(false)
        areaTemperatureController.openGates()
        setRegulateTempAvailable(false)
      }

      closeStructureBtn.setOnMouseClicked { _ =>
        openStructureBtn.setSelected(false)
        areaTemperatureController.closeGates()
        setRegulateTempAvailable(true)
      }

      minusTempBtn.setOnMouseClicked(_ => updateTempValue(_ - tempStep))
      plusTempBtn.setOnMouseClicked(_ => updateTempValue(_ + tempStep))
      regulateTempLabel.setText(areaTemperatureController.initialValue.toString)

      private def setRegulateTempAvailable(b: Boolean): Unit =
        minusTempBtn.setDisable(!b)
        plusTempBtn.setDisable(!b)
        regulateTempLabel.setDisable(!b)

      private def updateTempValue(f: Double => Double): Unit =
        val value = f(regulateTempLabel.getText.toDouble)
        areaTemperatureController.updTempValue(value)
        regulateTempLabel.setText(value.toString)
        checkRange(value)

      private def checkRange(v: Double): Unit =
        minusTempBtn.setDisable(v < tempRange._1)
        plusTempBtn.setDisable(v > tempRange._2)

  trait Interface extends Provider with Component:
    self: Requirements =>