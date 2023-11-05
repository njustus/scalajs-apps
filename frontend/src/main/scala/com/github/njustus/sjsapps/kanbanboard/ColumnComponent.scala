package com.github.njustus.sjsapps.kanbanboard

import com.github.njustus.sjsapps.kanbanboard.dtos.*
import com.github.njustus.sjsapps.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.{IO, SyncIO}
import japgolly.scalajs.react.facade.SyntheticDragEvent
import org.scalajs.dom

import java.time.LocalDate

object ColumnComponent {

  case class Props(state: TicketState, tickets: Seq[Ticket], changeTicketState: (TicketState, String) => IO[Unit])

  private case class State(draggedTicket: Option[Ticket] = None)

  private def renderTicket(onDragStart: Ticket => SyntheticDragEvent[_] => IO[Unit])(ticket: Ticket): VdomNode = {
    <.div(
      ^.className := "panel-block card",
      ^.draggable := true,
      ^.onDragStart ==> onDragStart(ticket),
      <.div(^.className := "card-content", ticket.name)
    )
  }

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    val onDragStart = (draggedTicket: Ticket) =>
      (ev: SyntheticDragEvent[_]) => {
        println(s"going to drag ticket: $draggedTicket")
        IO {
          ev.dataTransfer.setData("text", draggedTicket.id)
        }
      }

    val onDragStop = (ev: SyntheticDragEvent[_]) => {
      ev.preventDefault()
      val ticketId = ev.dataTransfer.getData("text")
      props.changeTicketState(props.state, ticketId)
    }

    <.div(
      ^.className := "column panel",
      ^.onDrop ==> onDragStop,
      ^.onDragOver ==> ((ev: SyntheticDragEvent[_]) => IO { ev.preventDefault() }),
      <.div(^.className := "panel-heading", props.state.toString),
      props.tickets.map(renderTicket(onDragStart)).toVdomArray
    )
  }

  val component = ScalaFnComponent
    .withHooks[Props]
    .useState(State())
    .render(renderFn)
}
