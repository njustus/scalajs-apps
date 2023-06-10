package com.github.njustus.sjsapps.memory

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions
import com.github.njustus.sjsapps.util._

object Memory {

  val symbols = Seq(
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

  case class Props(size:Int)

  def renderFn(): VdomNode = {
    val cards = Memory.symbols
      .flatMap(p => List(p, p))
      .map(p => MemoryCard.component(MemoryCard.Props(p, false)))

    <.div(^.className:="memory",
      <.div(^.className:="memory-grid",
        cards.toVdomArray
      )
    )
  }

  val component = ScalaFnComponent.withHooks[Unit]
    .render { (_) =>
      renderFn()
    }
}
