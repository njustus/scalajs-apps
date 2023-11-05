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

    val size: Int = countryToCapitals.size
  }

  case class GameState(
                        selectedCountry: Option[String],
                        selectedCapital: Option[String],
                        finishedCountries: Map[String, String]
                      ) {
                        val points = finishedCountries.size

                        def getCapitalModifier(name: String): Option[String] = 
                          val selectedModifier = selectedCapital.filter(_ == name).map(_ => "cell-selected")
                          selectedModifier.orElse {
                            finishedCountries.values.find(_ == name)
                          }

                        def getCountryModifier(name: String): Option[String] = 
                          val selectedModifier = selectedCountry.filter(_ == name).map(_ => "cell-selected")
                          selectedModifier.orElse {
                            finishedCountries.keys.find(_ == name)
                          }

  }

  object GameState {
    def hasWon(p: Props)(gs: GameState): Boolean = p.size == gs.points

    def selectCountry(country: String) = Focus[GameState](_.selectedCountry).replace(Some(country))
    def selectCapital(capital: String) = Focus[GameState](_.selectedCapital).replace(Some(capital))
  }

  def zero(p: Props): GameState = GameState(None, None, Map.empty)

  private def renderColumn(values: Seq[String], onClick: String => SyncIO[Unit], valueModifier: String => Option[String]): VdomArray = {
    values.zipWithIndex.map { (name, idx) =>
      val mod = valueModifier(name).getOrElse("")

      <.div(
        ^.key := idx,
        ^.className := s"cell $mod",
        ^.onClick --> onClick(name),
        name)
    }.toVdomArray
  }

  private def renderFn(props: Props, state: Hooks.UseState[GameState]): VdomNode = {
    val countryClicked = state.modState compose GameState.selectCountry
    val capitalClicked = state.modState compose GameState.selectCapital

    println(s"state: ${state.value}")

    if(GameState.hasWon(props)(state.value)) {
      <.div(^.className:="cell-finished", "Congrats you won")
    } else {
      <.div(^.className := "capital-guessing",
        <.div(),
        <.div(^.className:="score", "Points: ", state.value.points),
        <.div(
          renderColumn(props.countries, countryClicked, state.value.getCountryModifier)
        ),
        <.div(
          renderColumn(props.capitals, capitalClicked, state.value.getCapitalModifier)
        )
      )
    }
  }

  val component = ScalaFnComponent.withHooks[Props]
    .useStateBy(zero)
    .render(renderFn)
}
