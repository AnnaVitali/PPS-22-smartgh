package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.scene.layout.VBox

/** A trait that represents the select city view of the application. */
trait SelectCityView extends ViewComponent[VBox]

/** Object that can be used to create new instances of [[SelectCityView]]. */
object SelectCityView:

  /** Creates a new [[SelectCityView]] component.
    * @return
    *   a new instance of [[SelectCityView]]
    */
  def apply(): SelectCityViewImpl = SelectCityViewImpl()

  /** Implementation of [[SelectCityView]]. */
  class SelectCityViewImpl() extends AbstractViewComponent[VBox]("select_city.fxml") with SelectCityView:

    override val component: VBox = loader.load[VBox]
