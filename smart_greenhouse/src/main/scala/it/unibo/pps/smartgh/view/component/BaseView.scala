package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.layout.VBox
import javafx.scene.control.Label

/** A trait that represents the base view of the application. */
trait BaseView extends ViewComponent[VBox]

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

  /** Implementation of [[BaseView]].
    * @param title
    *   the title of the application
    * @param subtitle
    *   the subtitle of the application
    */
  private class BaseViewImpl(title: String, subtitle: String)
      extends AbstractViewComponent[VBox]("base.fxml")
      with BaseView:

    override val component: VBox = loader.load[VBox]

    @FXML
    var titleLabel: Label = _

    @FXML
    var subtitleLabel: Label = _

    titleLabel.setText(title)
    subtitleLabel.setText(subtitle)
