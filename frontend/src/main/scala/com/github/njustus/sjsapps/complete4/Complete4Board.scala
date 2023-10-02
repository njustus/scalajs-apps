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

 private def renderFn(state: Hooks.UseState[Complete4State]): VdomNode = {
    <.div(^.className:="complete-4",
      <.div(^.className:="complete-4-grid",
      state.value.chipColumns.flatMap { row =>
        row.map { chip =>
          ChipComponent.component(ChipComponent.Props(chip))
        }
      }.toVdomArray
      )
    )
  }

  val component = ScalaFnComponent.withHooks[Unit]
    .useState(Complete4State.zero)
    .render { (_, state) =>
      println(s"board: ${state.value}")
      renderFn(state)
    }
}
