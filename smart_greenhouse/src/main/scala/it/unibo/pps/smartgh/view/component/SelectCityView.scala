package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane

/** A trait that represents the select city view of the application. */
trait SelectCityView extends ViewComponent[BorderPane]

/** Object that can be used to create new instances of [[SelectCityView]]. */
object SelectCityView:

  /** Creates a new [[SelectCityView]] component.
    * @return
    *   a new instance of [[SelectCityView]]
    */
  def apply(): SelectCityView = SelectCityViewImpl()

  /** Implementation of [[SelectCityView]]. */
  private class SelectCityViewImpl() extends AbstractViewComponent[BorderPane]("select_city.fxml") with SelectCityView:

    override val component: BorderPane = loader.load[BorderPane]

    @FXML
    def nextClicked(): Unit =
      println("next clicked")
