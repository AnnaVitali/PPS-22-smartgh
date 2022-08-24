package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.component.SelectCityControllerModule
import it.unibo.pps.smartgh.controller.component.SelectCityControllerModule.SelectCityController
import it.unibo.pps.smartgh.mvc.SimulationMVC
import it.unibo.pps.smartgh.mvc.SimulationMVC.SimulationMVCImpl
import it.unibo.pps.smartgh.mvc.component.PlantSelectorMVC
import it.unibo.pps.smartgh.view.SimulationViewModule.SimulationView
import it.unibo.pps.smartgh.view.component.SelectPlantViewModule.SelectPlantView
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

    /** Class that contains the [[SelectCityView]] implementation.
      * @param simulationView
      *   the root view of the application.
      * @param baseView
      *   the view in which the [[SelectPlantView]] is enclosed.
      */
    class SelectCityViewViewImpl() extends AbstractViewComponent[BorderPane]("select_city.fxml") with SelectCityView:

      override val component: BorderPane = loader.load[BorderPane]
      var autoCompletionBinding: AutoCompletionBinding[String] = _

      //noinspection VarCouldBeVal
      @FXML
      var selectCityTextField: TextField = _

      //noinspection VarCouldBeVal
      @FXML
      var errorLabel: Label = _

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
          _ =>
            val selectedCity = selectCityTextField.getText.capitalize
            if selectedCity.isBlank then setErrorText("Please select a city")
            else if selectCityController.containCity(selectedCity) then
              selectCityController.saveCity(selectedCity)
              simulationMVC.simulationView.changeView(PlantSelectorMVC(simulationMVC).selectPlantView)
            else setErrorText("The selected city is not valid"),
          true
        )
      )

      private def setErrorText(text: String): Unit =
        Platform.runLater(() => errorLabel.setText(text))

  /** Trait that encloses the view for the city selection. */
  trait Interface extends Provider with Component:
    self: Requirements =>
