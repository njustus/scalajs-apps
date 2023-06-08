package com.github.njustus.sjsapps.memory

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions

object Memory {
  case class Props(size:Int)

  def renderFn(): VdomNode = {
    val cards = List.tabulate(8) {idx =>
      List.tabulate(8) {idx2 =>
        MemoryCard.component(MemoryCard.Props(s"A-$idx-$idx2", false))
      }
    }.flatten

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
