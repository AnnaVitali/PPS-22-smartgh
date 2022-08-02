package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.controller.CityController
import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.BorderPane
import org.controlsfx.control.textfield.{AutoCompletionBinding, TextFields}
import javafx.application.Platform

import java.util
import scala.jdk.javaapi.CollectionConverters.*

/** A trait that represents the select city view of the application. */
trait SelectCityView extends ViewComponent[BorderPane]:
  def autoCompletionBinding: AutoCompletionBinding[String]

/** Object that can be used to create new instances of [[SelectCityView]]. */
object SelectCityView:

  /** Creates a new [[SelectCityView]] component.
    * @param simulationView
    *   the [[SimulationView]] of the application
    * @param baseView
    *   the [[BaseView]] component
    * @return
    *   a new instance of [[SelectCityView]]
    */
  def apply(simulationView: SimulationView, baseView: BaseView): SelectCityView =
    SelectCityViewImpl(simulationView, baseView)

  private class SelectCityViewImpl(private val simulationView: SimulationView, private val baseView: BaseView)
      extends AbstractViewComponent[BorderPane]("select_city.fxml")
      with SelectCityView:

    override val component: BorderPane = loader.load[BorderPane]
    private val controller: CityController = CityController()
    var autoCompletionBinding: AutoCompletionBinding[String] = _

    @FXML
    var selectCityTextField: TextField = _

    @FXML
    var errorLabel: Label = _

    controller.view = this

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
