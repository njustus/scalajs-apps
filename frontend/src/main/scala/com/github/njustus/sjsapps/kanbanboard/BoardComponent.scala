package com.github.njustus.sjsapps.kanbanboard


import com.github.njustus.sjsapps.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.IO

import java.time.LocalDate


object BoardComponent {

  type State = dtos.Board

  private def renderFn(state: Hooks.UseState[State]): VdomNode = {
    <.div(^.className:="m-4 columns",
      state.value.sortedColumns
        .map((state, tickets) => ColumnComponent.component(ColumnComponent.Props(state,tickets)))
        .toVdomArray
    )
  }

  val component: PageComponent = ScalaFnComponent.withHooks[Unit]
    .useStateBy(props => dtos.board)
    .render((_, state) => renderFn(state))
}
