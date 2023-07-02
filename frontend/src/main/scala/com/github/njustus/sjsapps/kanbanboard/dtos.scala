package com.github.njustus.sjsapps.kanbanboard

object dtos {
  enum TicketState {
    case Todo extends TicketState
    case Wip extends TicketState
    case Test extends TicketState
    case Done extends TicketState
  }

  case class Board(tickets: Seq[Ticket]) {
    val columns: Map[TicketState, Seq[Ticket]] = tickets.groupBy(_.state)

    def moveState(ticketIdx: Int)(state: TicketState): Board = this.copy(tickets = tickets.zipWithIndex.map {
      case (ticket, idx) if idx == ticketIdx => ticket.copy(state = state)
      case (ticket, _) => ticket
    })
  }

  case class Ticket(name: String, state: TicketState)

  def board: Board = {
    val todos = List.tabulate(5) { idx => Ticket(s"tick-todo-$idx", TicketState.Todo) }
    val wip = List.tabulate(2) { idx => Ticket(s"ticket-wip-$idx", TicketState.Wip) }
    val done = List.tabulate(10) { idx => Ticket(s"obsolete done $idx", TicketState.Done) }

    Board(todos ++ wip ++ done)
  }
}
