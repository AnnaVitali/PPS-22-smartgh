package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.Label
import javafx.scene.layout.GridPane

/** A object that represent the area parameters view. */
object AreaParametersView:

  /** A trait that represents the generic view component for area parameters. */
  trait AreaParametersView extends ViewComponent[GridPane]:

    /** Update the parameter description with its min and max optimal values.
      * @param optimalValue
      *   the parameter optimal value
      */
    def updateDescription(optimalValue: String): Unit

    /** Update the current value of the parameter.
      * @param value
      *   the value detected
      * @param status
      *   the status of the parameter
      */
    def updateCurrentValue(value: String, status: String): Unit

  /** An abstract class that incapsulate the common methods for all parameters of the area.
    * @param fxmlFileName
    *   the name of the fxml file that is situated in /resources/fxml/ directory
    * @param parameter
    *   the parameter name
    * @tparam A
    *   the type of the FX component to wrap
    */
  abstract class AbstractAreaParametersView[A <: Parent](
      private val fxmlFileName: String,
      private val parameter: String
  ) extends AbstractViewComponent[A](fxmlFileName)
      with AreaParametersView:

    //noinspection VarCouldBeVal
    @FXML
    var descriptionLabel: Label = _

    //noinspection VarCouldBeVal
    @FXML
    var currentValueLabel: Label = _

    override def updateDescription(optimalValue: String): Unit =
      Platform.runLater(() => descriptionLabel.setText(parameter + " " + optimalValue))

    override def updateCurrentValue(value: String, status: String): Unit =
      Platform.runLater { () =>
        currentValueLabel.setText(value)
        currentValueLabel.getStyleClass.setAll(status + "State")
      }
