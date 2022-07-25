package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.SimulationView
import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.{Pane, StackPane, VBox}
import scalafx.scene.Parent

import java.net.URL
import java.util.ResourceBundle

trait BaseView extends ViewComponent[VBox]

object BaseView:

  def apply(view: SimulationView, title: String, subtitle: String): BaseViewImpl = BaseViewImpl(view, title, subtitle)

  class BaseViewImpl(view: SimulationView, title: String, subtitle: String)
      extends AbstractViewComponent[VBox]("base.fxml")
      with BaseView:

    override val component: VBox = loader.load[VBox]

    @FXML
    var titleLabel: Label = _

    @FXML
    var subtitleLabel: Label = _

    titleLabel.setText(title)
    subtitleLabel.setText(subtitle)
