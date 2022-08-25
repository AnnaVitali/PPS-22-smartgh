package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.controller.component.areaParameters.AreaTemperatureControllerModule
import it.unibo.pps.smartgh.controller.component.areaParameters.AreaTemperatureControllerModule.AreaTemperatureController
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.areaParameters.AreaParametersView.{
  AbstractAreaParametersView,
  AreaParametersView
}
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, ToggleButton}
import javafx.scene.layout.GridPane

/** Object that encloses the view module for the area temperature parameter. */
object AreaTemperatureViewModule:

  /** Trait that represents the area temperature parameter view. */
  trait AreaTemperatureView extends ViewComponent[GridPane] with AreaParametersView

  /** Trait that represents the provider of the area provider parameter. */
  trait Provider:

    /** The view of area temperature parameter. */
    val parameterView: AreaParametersView

  /** The view requirements. */
  type Requirements = AreaTemperatureControllerModule.Provider

  /** Trait that represents the components of the view for the area temperature parameter. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[AreaTemperatureView]] implementation. */
    class AreaTemperatureViewImpl()
        extends AbstractAreaParametersView[GridPane]("area_temperature.fxml", "Temperature")
        with AreaTemperatureView:

      override val component: GridPane = loader.load[GridPane]
      private val tempStep = 0.5
      private val tempRange = (10.0 + tempStep, 40.0 - tempStep)
      private val areaTemperatureController = parameterController.asInstanceOf[AreaTemperatureController]

      //noinspection VarCouldBeVal
      @FXML
      var openStructureBtn: ToggleButton = _

      //noinspection VarCouldBeVal
      @FXML
      var closeStructureBtn: ToggleButton = _

      //noinspection VarCouldBeVal
      @FXML
      var regulateTempLabel: Label = _

      //noinspection VarCouldBeVal
      @FXML
      var minusTempBtn: Button = _

      //noinspection VarCouldBeVal
      @FXML
      var plusTempBtn: Button = _

      regulateTempLabel.setText(areaTemperatureController.temperature.toString)
      initGatesBtn(areaTemperatureController.isGatesOpen)

      openStructureBtn.setOnMouseClicked { _ =>
        if openStructureBtn.isSelected then
          areaTemperatureController.openGates()
          setRegulateTempAvailable(false)
        else openStructureBtn.setSelected(true)
      }

      closeStructureBtn.setOnMouseClicked { _ =>
        if closeStructureBtn.isSelected then
          areaTemperatureController.closeGates()
          setRegulateTempAvailable(true)
        else closeStructureBtn.setSelected(true)
      }

      minusTempBtn.setOnMouseClicked(_ => updateTempValue(_ - tempStep))
      plusTempBtn.setOnMouseClicked(_ => updateTempValue(_ + tempStep))

      private def initGatesBtn(isOpen: Boolean): Unit =
        openStructureBtn.setSelected(isOpen)
        closeStructureBtn.setSelected(!isOpen)
        setRegulateTempAvailable(!isOpen)

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

  /** Trait that encloses the view for area temperature parameter. */
  trait Interface extends Provider with Component:
    self: Requirements =>