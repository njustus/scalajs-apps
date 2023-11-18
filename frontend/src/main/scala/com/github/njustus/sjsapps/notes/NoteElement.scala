package com.github.njustus.sjsapps.notes

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.*
import org.scalajs.dom
import com.github.njustus.sjsapps.util.*

object NoteElement {

  type Props = NotesWrapper.Note

  private def renderFn(props: Props): VdomNode = {
    <.div(
      ^.className := "note",
      <.div(^.className := "note-text", props.text),
      <.div(^.className := "note-info", s"Created: ${formatting.formatDateTime(props.createdAt)}")
    )
  }

  val component = ScalaFnComponent
    .withHooks[Props]
    .render(props => renderFn(props))
}
