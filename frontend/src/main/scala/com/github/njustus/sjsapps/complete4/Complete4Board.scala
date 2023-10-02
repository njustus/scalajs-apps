package com.github.njustus.sjsapps.complete4

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

object Complete4Board {
  def renderFn(state: Hooks.UseState[Complete4State]): VdomNode = {
    <.div(^.className:="complete-4",
      "test complete-4")
  }

  val component = ScalaFnComponent.withHooks[Unit]
    .useState(Complete4State.zero)
    .render { (_, state) =>
      renderFn(state)
    }
}
