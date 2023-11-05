package com.github.njustus.sjsapps.kanbanboard

import cats.effect.IO
import cats.effect.std.UUIDGen

import scala.util.Random

object dtos {
  enum TicketState(val orderId: Int) extends Ordered[TicketState] {
    case Todo extends TicketState(0)
    case Wip  extends TicketState(1)
    case Test extends TicketState(2)
    case Done extends TicketState(3)

    override def compare(that: TicketState): Int = this.orderId - that.orderId
  }

  case class Board(tickets: Seq[Ticket]) {
    private val columns: Map[TicketState, Seq[Ticket]] = tickets.groupBy(_.state)
    val sortedColumns: Seq[(TicketState, Seq[Ticket])] = columns.toList.sortBy((state, _) => state)

    def moveStateById(state: TicketState)(ticketId: String): Board = this.copy(tickets = tickets.map {
      case originalTicket if originalTicket.id == ticketId => originalTicket.copy(state = state)
      case ticket                                          => ticket
    })
  }

  case class Ticket(name: String, state: TicketState, id: String = Random.alphanumeric.take(8).mkString)

  def board: Board = {
    val todos = List.tabulate(5) { idx => Ticket(s"tick-todo-$idx", TicketState.Todo) }
    val wip   = List.tabulate(2) { idx => Ticket(s"ticket-wip-$idx", TicketState.Wip) }
    val done  = List.tabulate(10) { idx => Ticket(s"obsolete done $idx", TicketState.Done) }

    Board(todos ++ wip ++ done)
  }
}
