package com.github.njustus.sjsapps.kanbanboard

import com.github.njustus.sjsapps.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.IO
import com.github.njustus.sjsapps.kanbanboard.dtos._

object BoardComponent {

  type State = dtos.Board

  private def renderFn(state: Hooks.UseState[State]): VdomNode = {
    def updateTicketColumn(ticketState: TicketState, ticketId: String): IO[Unit] = {
      println(s"moving $ticketId into state $ticketState")
      val fn: State => State = _.moveStateById(ticketState)(ticketId)
      state.modState(fn).to[IO]
    }

    <.div(
      ^.className := "m-4 columns",
      state.value.sortedColumns
        .map((state, tickets) => ColumnComponent.component(ColumnComponent.Props(state, tickets, updateTicketColumn)))
        .toVdomArray
    )
  }

  val component: PageComponent = ScalaFnComponent
    .withHooks[Unit]
    .useStateBy(props => dtos.board)
    .render((_, state) => renderFn(state))
}
