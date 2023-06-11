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

  case class State(board: List[MemoryCard.Props])

  def zero: Memory.State = {
    val cards = Memory.symbols
      .flatMap { icon =>
        val id = icon.icon
        List(
          MemoryCard.Props(id+"-1", icon, false),
          MemoryCard.Props(id+"-2", icon, false)
        )
      }

    require(cards.distinctBy(_.id).size == cards.size, "Expected unique IDS per card")

    State(Random.shuffle(Random.shuffle(cards)))
  }

  def renderFn(state: Hooks.UseState[State]): VdomNode = {
    val cardComponents = state.value.board.map { props =>
      <.div(^.key:=props.id,MemoryCard.component(props))
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
