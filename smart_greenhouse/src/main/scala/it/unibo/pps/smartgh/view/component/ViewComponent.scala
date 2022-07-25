package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent
import javafx.fxml.FXMLLoader
import javafx.scene.Parent

trait ViewComponent[A <: Parent]:
  val component: A

object ViewComponent:

  given toComponent[A <: Parent]: Conversion[ViewComponent[A], A] with
    override def apply(component: ViewComponent[A]): A = component.component

  abstract class AbstractViewComponent[A <: Parent](fxmlFileName: String) extends ViewComponent[A]:
    protected val loader: FXMLLoader = FXMLLoader()
    loader.setController(this)
    loader.setLocation(getClass.getResource("/fxml/" + fxmlFileName))
