package com.github.njustus.sjsapps.memory

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions
import com.github.njustus.sjsapps.util._
import scala.util.Random
import scala.concurrent.duration.*
import com.github.njustus.sjsapps.memory.MemoryState.Player

object Memory {

  private def displayWinner(winner: Player): VdomNode =
    <.div(
      ^.className := "",
      <.h2(^.className := "has-text-primary is-size-1 strong", s"${winner.name} won with ${winner.points} Points!")
    )

  def renderFn(state: Hooks.UseState[MemoryState]): VdomNode = {
    def onCardClicked(key: String): IO[Unit] =
      println(s"clicked key: $key")
      state.modState { _.updateCard(key) { card => card.flip } }.to[IO]
        >> IO.sleep(2.seconds)
        >> state.modState { _.checkOpenCards() }.to[IO]

    val cardComponents = state.value.board.map { cardDto =>
      <.div(^.key := cardDto.id, MemoryCard.component(MemoryCard.Props(cardDto, onCardClicked)))
    }

    state.value.winner.map(displayWinner).getOrElse {
      <.div(
        ^.className := "columns memory",
        <.div(^.className := "column is-two-thirds", <.div(^.className := "memory-grid", cardComponents.toVdomArray)),
        <.div(^.className := "column", ScoreDisplay.component(ScoreDisplay.Props(state.value)))
      )
    }
  }

  val component = ScalaFnComponent
    .withHooks[Unit]
    .useState(MemoryState.zero)
    .render { (_, state) =>
      renderFn(state)
    }
}
