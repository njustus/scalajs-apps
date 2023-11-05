package com.github.njustus.sjsapps.capitalguessing

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom

object CapitalGuessingGame {

  case class Props(countryToCapitals: Map[String, String])
  
  case class GameState()

  def zero(p:Props): GameState = {
    GameState()
  }
  
  private def renderFn(props: Props, state: Hooks.UseState[GameState]): VdomNode = {
    <.div(s"guessing game: ${props.countryToCapitals}")
  }

  val component = ScalaFnComponent.withHooks[Props]
    .useStateBy(zero)
    .render(renderFn)
}
