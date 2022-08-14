package it.unibo.pps.smartgh.view.component

import it.unibo.pps.smartgh.view.component.ViewComponent.AbstractViewComponent
import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import scalafx.scene.Scene
import scalafx.Includes.*

/** A trait that represents the view for user guide. */
trait HelpView extends ViewComponent[BorderPane]:

    /** Method to close the help view window */
    def closeWindow() : Unit

/** Object that can be used to create new instances of [[HelpView]]. */
object HelpView:

    /**
      * Apply method to create a new [[HelpView]] component.
      * @param stage
      *     stage which will contain the new help view
      * */
    def apply(stage: Stage) : HelpView = HelpViewImpl(stage)

    private class HelpViewImpl(stage: Stage) extends AbstractViewComponent[BorderPane]("help.fxml") with HelpView:

        override val component: BorderPane = loader.load[BorderPane]

        stage.resizable = true
        private val scene: Scene = Scene(stage.width.value, stage.height.value)
        scene.root.value = this
        stage.scene = scene
        stage.show()

        @FXML
        var helpTextArea : TextArea = _

        val helpText : String = "prova"

        helpTextArea.setText(helpText)

        override def closeWindow(): Unit =
            stage.close()