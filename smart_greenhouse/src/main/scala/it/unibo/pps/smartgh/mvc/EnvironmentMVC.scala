package it.unibo.pps.smartgh.mvc

class EnvironmentMVC
  extends EnvironmentModelModule.Interface
    with EnvironmentViewModule.Interface
    with EnvironmentControllerModule.Interface:

  override val model: EnvironmentModelModule.Model = ???
  override val view: EnvironmentViewModule.View = ???
  override val controller: EnvironmentControllerModule.Controller = ???
