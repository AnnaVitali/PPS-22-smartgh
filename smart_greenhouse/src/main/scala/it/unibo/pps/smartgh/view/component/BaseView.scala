package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import javafx.scene.control.{Button, Label}

/** A trait that represents the base view of the application. */
trait BaseView extends ViewComponent[BorderPane]:
  var changeSceneButton: Button

/** Object that can be used to create new instances of [[BaseView]]. */
object BaseView:

  /** Creates a new [[BaseView]] component that contains the common parts of all layouts.
    * @param title
    *   the title of the application
    * @param subtitle
    *   the subtitle of the application
    * @return
    *   a new instance of [[BaseView]]
    */
  def apply(title: String, subtitle: String): BaseView = BaseViewImpl(title, subtitle)

  private class BaseViewImpl(title: String, subtitle: String)
      extends AbstractViewComponent[BorderPane]("base.fxml")
      with BaseView:

    override val component: BorderPane = loader.load[BorderPane]

    @FXML
    var titleLabel: Label = _

    @FXML
    var subtitleLabel: Label = _

    @FXML
    override var changeSceneButton: Button = _

    titleLabel.setText(title)
    subtitleLabel.setText(subtitle)
