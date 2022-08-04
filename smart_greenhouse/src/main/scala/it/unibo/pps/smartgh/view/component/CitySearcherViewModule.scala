package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.CitySearcherControllerModule
import it.unibo.pps.smartgh.controller.CitySearcherControllerModule.CitySearcherController
import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.BorderPane
import org.controlsfx.control.textfield.{AutoCompletionBinding, TextFields}
import javafx.application.Platform

import java.util
import scala.jdk.javaapi.CollectionConverters.*

/** Object that can be used to create new instances of [[SelectCityView]]. */
object CitySearcherViewModule:

  /** A trait that represents the select city view of the application. */
  trait CitySearcherView extends ViewComponent[BorderPane]:
    def autoCompletionBinding: AutoCompletionBinding[String]

  trait Provider:
    val citySearcherView: CitySearcherView

  type Requirements = CitySearcherControllerModule.Provider

  trait Component:
    context: Requirements =>
    class CitySearcherViewViewImpl(private val simulationView: SimulationView, private val baseView: BaseView)
        extends AbstractViewComponent[BorderPane]("select_city.fxml")
        with CitySearcherView:

      override val component: BorderPane = loader.load[BorderPane]
      private val controller: CitySearcherController = context.citySearcherController
      var autoCompletionBinding: AutoCompletionBinding[String] = _

      @FXML
      var selectCityTextField: TextField = _

      @FXML
      var errorLabel: Label = _

      autoCompletionBinding = TextFields
        .bindAutoCompletion(
          selectCityTextField,
          text => {
            if !errorLabel.getText.isBlank then setErrorText("")
            asJavaCollection(controller.searchCities(text.getUserText().capitalize))
          }
        )

      autoCompletionBinding.setDelay(0)

      baseView.changeSceneButton.setText("Next")
      baseView.changeSceneButton.setOnMouseClicked { _ =>
        val selectedCity = selectCityTextField.getText.capitalize
        if selectedCity.isBlank then setErrorText("Please select a city")
        else if controller.containCity(selectedCity) then
          controller.saveCity(selectedCity)
          simulationView.changeView(SelectPlantView(simulationView, baseView))
        else setErrorText("The selected city is not valid")
      }

      private def setErrorText(text: String): Unit =
        Platform.runLater(() => errorLabel.setText(text))

  trait Interface extends Provider with Component:
    self: Requirements =>
