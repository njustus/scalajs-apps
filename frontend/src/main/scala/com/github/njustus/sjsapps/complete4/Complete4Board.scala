package com.github.njustus.sjsapps.complete4

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import org.scalajs.dom.{console, window}
import cats.effect.*
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions
import com.github.njustus.sjsapps.util.*

import scala.util.Random
import scala.concurrent.duration.*
import com.github.njustus.sjsapps.memory.MemoryState.Player

object Complete4Board {

  private def playerState(state: Complete4State): VdomNode = {
    <.div(^.className := "columns",
      state.players.map { player =>
        val currentPlayerClass = if (state.isCurrentPlayer(player)) "has-text-weight-bold" else ""
        <.div(
          ^.key := player.name,
          ^.className := s"column is-full ${player.descriptionCssClass} ${currentPlayerClass}",
          (if (state.isCurrentPlayer(player)) "* " else "") + player.name)
      }.toVdomArray
    )
  }

  private def renderFn(state: Hooks.UseState[Complete4State]): VdomNode = {
    def onClick(columnIdx: Int): IO[Unit] =
      state.modState(_.addChip(columnIdx)).to[IO]

    <.div(^.className := "complete-4",
      <.div(^.className := "complete-4-grid",
        state.value.chipColumns.zipWithIndex.map { (col, colIdx) =>
          <.div(^.key := s"column-$colIdx",
            col.map { chip =>
              <.div(
                ^.key := chip.id,
                ^.id := s"chip-${chip.id}",
                ChipComponent.component(ChipComponent.Props(chip, colIdx, onClick))
              )
            }.reverse.toVdomArray
          )
        }.toVdomArray
      ),
      <.div(playerState(state.value))
    )
  }

  val component = ScalaFnComponent.withHooks[Unit]
    .useState(Complete4State.zero)
    .render { (_, state) =>
      println(s"board: ${state.value}")
      renderFn(state)
    }
}
