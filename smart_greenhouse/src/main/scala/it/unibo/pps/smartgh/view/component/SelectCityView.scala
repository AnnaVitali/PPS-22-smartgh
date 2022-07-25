package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.scene.layout.VBox

trait SelectCityView extends ViewComponent[VBox]

object SelectCityView:

  def apply(): SelectCityViewImpl = SelectCityViewImpl()

  class SelectCityViewImpl() extends AbstractViewComponent[VBox]("select_city.fxml") with SelectCityView:

    override val component: VBox = loader.load[VBox]
