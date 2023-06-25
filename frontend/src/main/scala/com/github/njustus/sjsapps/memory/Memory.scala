package com.github.njustus.sjsapps.memory

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions
import com.github.njustus.sjsapps.util._
import scala.util.Random

object Memory {

  def renderFn(state: Hooks.UseState[MemoryState]): VdomNode = {
    def onCardClicked(key:String):SyncIO[Unit] =
      println(s"clicked key: $key")
      state.modState(_.updateCard(key) { card => card.flip })

    val cardComponents = state.value.board.map { cardDto =>
      <.div(^.key:=cardDto.id,
        MemoryCard.component(MemoryCard.Props(cardDto, onCardClicked))
      )
    }

    <.div(^.className:="memory",
      <.div(^.className:="memory-grid",
        cardComponents.toVdomArray
      )
    )
  }

  val component = ScalaFnComponent.withHooks[Unit]
    .useState(MemoryState.zero)
    .render { (_, state) =>
      renderFn(state)
    }
}
