package com.github.njustus.sjsapps.kanbanboard

import com.github.njustus.sjsapps.kanbanboard.dtos._

import com.github.njustus.sjsapps.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.IO

import java.time.LocalDate

object ColumnComponent {

  case class Props(state: TicketState, tickets:Seq[Ticket])

  private def renderTicket(ticket: Ticket): VdomNode = {
    <.div(^.className:="panel-block card",
      <.div(^.className:="card-content", ticket.name)
    )
  }

  private def renderFn(props: Props): VdomNode = {
    <.div(^.className:="column panel",
      <.div(^.className:="panel-heading", props.state.toString),

        props.tickets.map(renderTicket).toVdomArray

    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .render(props => renderFn(props))
}
