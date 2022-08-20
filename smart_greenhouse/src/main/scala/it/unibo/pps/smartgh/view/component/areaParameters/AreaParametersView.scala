package it.unibo.pps.smartgh.view.component.areaParameters

import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.Parent
import javafx.scene.control.Label

object AreaParametersView:

  trait AreaParametersView:
    def updateDescription(optimalValue: String): Unit
    def updateCurrentValue(value: String, status: String): Unit

  abstract class AbstractAreaParametersView[A <: Parent](fxmlFileName: String, parameter: String)
      extends AbstractViewComponent[A](fxmlFileName)
      with AreaParametersView:

    @FXML
    var descriptionLabel: Label = _

    @FXML
    var currentValueLabel: Label = _

    override def updateDescription(optimalValue: String): Unit =
      descriptionLabel.setText(parameter + " " + optimalValue)

    override def updateCurrentValue(value: String, status: String): Unit =
      Platform.runLater { () =>
        currentValueLabel.setText(value)
        currentValueLabel.getStyleClass.setAll(status + "State")
      }
