package com.github.njustus.sjsapps.notes

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom

object NoteSearch {

  case class Props(updateSearch: String => IO[Unit])

  case class State(searchInput: String = "")

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    def updateInput(ev: ReactEventFromInput) = {
      val v = ev.target.value
      state.modState(_.copy(v))
    }

    <.div(^.className:="columns",
      <.input(^.className := "column is-9 input",
        ^.placeholder := "Search",
        ^.onChange ==> updateInput),
        <.button(^.className := "button is-primary",
          "Search",
          ^.onClick --> props.updateSearch(state.value.searchInput))
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .useState(State())
    .render(renderFn)
}
