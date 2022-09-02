package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent
import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.BorderPane

/** A trait that represents the base view of the application. */
trait BaseView extends ViewComponent[BorderPane]:
  /** Change scene button. */
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

  private class BaseViewImpl(private val title: String, private val subtitle: String)
      extends AbstractViewComponent[BorderPane]("base.fxml")
      with BaseView:

    //noinspection VarCouldBeVal
    @FXML
    protected var titleLabel: Label = _

    //noinspection VarCouldBeVal
    @FXML
    protected var subtitleLabel: Label = _

    @FXML
    override var changeSceneButton: Button = _

    titleLabel.setText(title)
    subtitleLabel.setText(subtitle)
