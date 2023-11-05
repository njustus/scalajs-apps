package com.github.njustus.sjsapps.capitalguessing

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom

object GuessingGameWrapper {

  type Props = Unit

  case class State(guessingProps: CapitalGuessingGame.Props)

  def zero = {
    val m = Map(
      "Germany" -> "Berlin",
      "France" -> "Paris",
      "Italy" -> "Rome",
      "USA" -> "Washington"
    )

    State(CapitalGuessingGame.Props(m))
  }

  private def renderFn(state: Hooks.UseState[State]): VdomNode = {
    <.div(
      CapitalGuessingGame.component(state.value.guessingProps)
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .useState(zero)
    .render((_, st) => renderFn(st))
}
