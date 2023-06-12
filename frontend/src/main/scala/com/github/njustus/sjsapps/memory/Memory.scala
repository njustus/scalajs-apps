package com.github.njustus.sjsapps.memory

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions
import com.github.njustus.sjsapps.util._
import scala.util.Random

object Memory {

  val symbols = List(
    Icon.Props("snowflake"),
    Icon.Props("snowman"),
    Icon.Props("skiing"),
    Icon.Props("mountain"),
    Icon.Props("music"),
    Icon.Props("star"),
    Icon.Props("heart"),
    Icon.Props("hippo"),
    Icon.Props("cat"),
    Icon.Props("feather"),
    Icon.Props("dragon"),
    Icon.Props("otter"),
    Icon.Props("frog"),
    Icon.Props("horse"),
    Icon.Props("crow"),
  )

  case class State(board: List[CardDto])

  def zero: Memory.State = {
    val cards = Memory.symbols
      .flatMap { icon =>
        val id = icon.icon
        List(
          CardDto(id+"-1", icon, CardState.Closed),
          CardDto(id+"-2", icon, CardState.Closed)
        )
      }

    require(cards.distinctBy(_.id).size == cards.size, "Expected unique IDS per card")

    State(Random.shuffle(Random.shuffle(cards)))
  }

  def renderFn(state: Hooks.UseState[State]): VdomNode = {
    def onCardClicked(key:String):SyncIO[Unit] = {
      println(s"clicked key: $key")
      state.modState(st =>
        st.copy(board = st.board.map {
        case card if card.id == key => card.flip
        case card => card
      })
      )
    }


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
  .useState(zero)
    .render { (_, state) =>
      renderFn(state)
    }
}
