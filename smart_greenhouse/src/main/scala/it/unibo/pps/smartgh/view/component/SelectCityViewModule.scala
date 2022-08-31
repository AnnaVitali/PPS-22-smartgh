package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.SelectCityControllerModule
import it.unibo.pps.smartgh.controller.component.SelectCityControllerModule.SelectCityController
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.component.PlantSelectorMVC
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.BorderPane
import org.controlsfx.control.textfield.{AutoCompletionBinding, TextFields}

import java.util
import scala.jdk.javaapi.CollectionConverters.*

/** Object that encloses the view module for the city selection. */
object SelectCityViewModule:

  /** A trait that represents the select city scene of the application. */
  trait SelectCityView extends ViewComponent[BorderPane]:

    /** Set and show the error message.
      * @param text
      *   the error message
      */
    def setErrorText(text: String): Unit

    /** Show the next scene */
    def showNextScene(): Unit

    /** The [[AutoCompletionBinding]] component. */
    def autoCompletionBinding: AutoCompletionBinding[String]

  /** Trait that represents the provider of the view for the city selection. */
  trait Provider:

    /** The view of select city. */
    val selectCityView: SelectCityView

  /** The view requirements. */
  type Requirements = SelectCityControllerModule.Provider with SimulationMVC.Provider

  /** Trait that represents the components of the view for the city selection. */
  trait Component:
    context: Requirements =>

    /** Class that contains the [[SelectCityView]] implementation. */
    class SelectCityViewViewImpl() extends AbstractViewComponent[BorderPane]("select_city.fxml") with SelectCityView:

      var autoCompletionBinding: AutoCompletionBinding[String] = _

      //noinspection VarCouldBeVal
      @FXML
      protected var selectCityTextField: TextField = _

      //noinspection VarCouldBeVal
      @FXML
      protected var errorLabel: Label = _

      autoCompletionBinding = TextFields
        .bindAutoCompletion(
          selectCityTextField,
          text => {
            if !errorLabel.getText.isBlank then setErrorText("")
            asJavaCollection(selectCityController.searchCities(text.getUserText().capitalize))
          }
        )

      autoCompletionBinding.setDelay(0)

      Platform.runLater(() =>
        simulationMVC.simulationView.changeSceneButtonBehaviour(
          "Next",
          _ => selectCityController.checkCity(selectCityTextField.getText.capitalize)
        )
      )

      override def setErrorText(text: String): Unit = Platform.runLater(() => errorLabel.setText(text))

      override def showNextScene(): Unit =
        simulationMVC.simulationView.changeView(PlantSelectorMVC(simulationMVC).selectPlantView)

  /** Trait that encloses the view for the city selection. */
  trait Interface extends Provider with Component:
    self: Requirements =>
