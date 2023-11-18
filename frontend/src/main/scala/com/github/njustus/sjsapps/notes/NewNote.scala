package com.github.njustus.sjsapps.notes

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom

import java.time.Instant

object NewNote {

  val MAX_NOTE_SIZE = 120

  case class Props(newNote: NotesWrapper.Note => IO[Unit])

  case class State(text: String = "") {
    def length: Int = text.length

    def toNote: NotesWrapper.Note = {
      val instant = Instant.now
      NotesWrapper.Note(s"id-${instant.toEpochMilli}", text, instant)
    }
  }

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    def updateInput(ev: ReactEventFromInput): IO[Unit] = {
      val v = ev.target.value
      if (v.length > MAX_NOTE_SIZE) IO.unit
      else state.modState(_.copy(v)).to[IO]
    }

    <.div(
      ^.className := "note edit-note",
      <.div(
        ^.className := "note-text",
        <.textarea(
          ^.className   := "input text-input",
          ^.maxLength   := MAX_NOTE_SIZE,
          ^.placeholder := "your text here",
          ^.onChange ==> updateInput
        )
      ),
      <.div(
        ^.className := "note-info columns",
        <.div(^.className := "column is-9", s"Length: ${state.value.length}/$MAX_NOTE_SIZE"),
        <.div(
          ^.className := "column",
          <.button(^.className := "button is-small is-primary", "Add", ^.onClick --> props.newNote(state.value.toNote))
        )
      )
    )
  }

  val component = ScalaFnComponent
    .withHooks[Props]
    .useState(State())
    .render(renderFn)
}
