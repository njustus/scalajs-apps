package com.github.njustus.sjsapps.notes

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom

object NewNote {

  type Props = Unit
  case class State(text: String="") {
    def length: Int = text.length
  }

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    <.div(^.className:="note edit-note",
      <.div(^.className:="note-text",
        <.textarea(^.className:="input text-input", ^.placeholder:= "your text here")
      ),
      <.div(^.className:="note-info columns",
        <.div(^.className:="column is-9", s"Length: ${state.value.length}/120"),
        <.div(^.className:="column",
          <.button(^.className:="button is-small is-primary", "Add")
        )
      )
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .useState(State())
    .render(renderFn)
}
