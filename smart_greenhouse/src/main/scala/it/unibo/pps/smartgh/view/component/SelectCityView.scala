package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.city.{CitiesSearcher, City, CityController}
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
    * @return
    *   a new instance of [[SelectCityView]]
    */
  def apply(): SelectCityView = SelectCityViewImpl()

  /** Implementation of [[SelectCityView]]. */
  private class SelectCityViewImpl extends AbstractViewComponent[BorderPane]("select_city.fxml") with SelectCityView:

    override val component: BorderPane = loader.load[BorderPane]
    private val controller: CityController = CityController()
    var autoCompletionBinding: AutoCompletionBinding[String] = _

    @FXML
    var selectCityTextField: TextField = _

    @FXML
    var errorLabel: Label = _

    @FXML
    var nextButton: Button = _

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

    @FXML
    def nextClicked(): Unit =
      val selectedCity = selectCityTextField.getText.capitalize
      if selectedCity.isBlank then setErrorText("Please select a city")
      else if controller.containCity(selectedCity) then controller.saveCity(selectedCity)
      else setErrorText("The selected city is not valid")

    private def setErrorText(text: String): Unit =
      Platform.runLater(() => errorLabel.setText(text))
