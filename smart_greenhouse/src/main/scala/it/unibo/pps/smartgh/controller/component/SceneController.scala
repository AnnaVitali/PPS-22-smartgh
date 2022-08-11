package it.unibo.pps.smartgh.controller.component

import it.unibo.pps.smartgh.view.component.BaseView

trait SceneController:

  /** Method that asks the controller to instantiate the next MVC, for the next scene.
    *
    * @param baseView
    *   the template view for the next scene.
    */
  def instantiateNextSceneMVC(baseView: BaseView): Unit
