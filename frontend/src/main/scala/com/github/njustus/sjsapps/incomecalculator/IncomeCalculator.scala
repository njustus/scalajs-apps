package com.github.njustus.sjsapps.incomecalculator

import com.github.njustus.sjsapps.*
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object IncomeCalculator {
  case class State()

  private def renderFn(state: Hooks.UseState[State]): VdomNode = {
    <.div(
      "income-calculator"
    )
  }

  val component: PageComponent = ScalaFnComponent.withHooks[Unit]
    .useStateBy(props => State())
    .render((_, state) => renderFn(state))
}
