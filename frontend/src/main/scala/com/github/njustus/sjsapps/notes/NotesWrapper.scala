package com.github.njustus.sjsapps.notes

import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.*
import japgolly.scalajs.react.facade.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import org.scalajs.dom

import java.time.temporal.*
import java.time.{Instant, LocalDateTime, ZoneId}

object NotesWrapper {

  val example: Seq[Note] = for {
    day <- 10 to 1 by -1
  } yield Note(s"test-$day",
    s"lorem ipsum sit $day",
    Instant.now().minus(day, ChronoUnit.DAYS)
  )

  type Props = Unit

  case class Note(id: String,
                  text: String,
                  createdAt: Instant)

  case class State(search: Option[String],
                   notes: Seq[Note]) {

    def filteredNotes: Seq[Note] = search match {
      case Some(txt) => notes.filter(n => n.text.contains(txt))
      case None => notes
    }
  }

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    def handleSearchUpdate(search: String): IO[Unit] =
      if(search.isBlank) state.modState(_.copy(search = None)).to[IO]
      else state.modState(_.copy(search = Some(search.trim))).to[IO]

    <.div(^.className := "notes-wrapper",
      <.div(^.className:="search",
        NoteSearch.component(NoteSearch.Props(handleSearchUpdate))
      ),
        <.div(^.className := "notes-grid",
          state.value.filteredNotes.map { note =>
            <.div(
              ^.key := note.id,
              NoteElement.component(note))
          }.toVdomArray,
          NewNote.component()
        )
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .useState(State(None, example))
    .render(renderFn)
}
