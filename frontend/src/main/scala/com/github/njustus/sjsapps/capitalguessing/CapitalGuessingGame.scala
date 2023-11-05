package com.github.njustus.sjsapps.capitalguessing

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom

import scala.util.Random

object CapitalGuessingGame {

  case class Props(countryToCapitals: Map[String, String]) {
    lazy val countries: Seq[String] = Random.shuffle(countryToCapitals.keys.toSeq)
    lazy val capitals: Seq[String] = Random.shuffle(countryToCapitals.values.toSeq)
  }

  case class GameState()

  def zero(p:Props): GameState = {
    GameState()
  }

  private def renderColumn(values: Seq[String]): VdomArray = {
    values.zipWithIndex.map { (name, idx) =>
      <.div(
        ^.className:= "cell",
        name)
    }.toVdomArray
  }

  private def renderFn(props: Props, state: Hooks.UseState[GameState]): VdomNode = {
    <.div(^.className := "capital-guessing",
      <.div(
        renderColumn( props.countries)
      ),
      <.div(
        renderColumn( props.capitals)
      )
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .useStateBy(zero)
    .render(renderFn)
}
