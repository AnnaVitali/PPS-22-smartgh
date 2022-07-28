package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.city.{CitiesSearcher, City, CityController}
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.BorderPane
import org.controlsfx.control.textfield.{AutoCompletionBinding, TextFields}

import java.util
import scala.jdk.javaapi.CollectionConverters.*

/** A trait that represents the select city view of the application. */
trait SelectCityView extends ViewComponent[BorderPane]

/** Object that can be used to create new instances of [[SelectCityView]]. */
object SelectCityView:

  /** Creates a new [[SelectCityView]] component.
    * @param citiesSearcher
    *   a [[CitiesSearcher]] for searcher cities.
    * @return
    *   a new instance of [[SelectCityView]]
    */
  def apply(citiesSearcher: CitiesSearcher): SelectCityView = SelectCityViewImpl(citiesSearcher)

  /** Implementation of [[SelectCityView]]. */
  private class SelectCityViewImpl(citiesSearcher: CitiesSearcher)
      extends AbstractViewComponent[BorderPane]("select_city.fxml")
      with SelectCityView:

    private val controller = CityController()
    controller.view = this

    override val component: BorderPane = loader.load[BorderPane]

    @FXML
    var selectCityTextField: TextField = _

    TextFields
      .bindAutoCompletion(
        selectCityTextField,
        text => asJavaCollection(citiesSearcher.searchCities(text.getUserText().capitalize))
      )
      .setDelay(0)

    @FXML
    def nextClicked(): Unit =
      println("[next clicked] selected city: " + selectCityTextField.getText)
      controller.saveCity(selectCityTextField.getText)
