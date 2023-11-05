package com.github.njustus.sjsapps

import com.github.njustus.sjsapps.capitalguessing.GuessingGameWrapper
import com.github.njustus.sjsapps.complete4.Complete4Board
import com.github.njustus.sjsapps.incomecalculator.IncomeCalculator
import com.github.njustus.sjsapps.kanbanboard.BoardComponent
import com.github.njustus.sjsapps.tippscalculator.TippsCalculator
import com.github.njustus.sjsapps.memory.Memory
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.*
import japgolly.scalajs.react.vdom.html_<^.*

object PageWrapper {
  case class Props(pages: Map[String, PageComponent])

  case class State(selectedKey:String)

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    val menuItems = props.pages.keys.toList.sorted.map { name =>
      val className = if(state.value.selectedKey==name) "is-active" else ""

      def selectPage(name:String)(ev: ReactEvent) = {
        ev.preventDefault()
        state.modState(_.copy(selectedKey = name))
      }

      <.li(^.key:=name,
        <.a(^.href:="#",
          ^.className:=className,
          ^.onClick ==>selectPage(name),
          name)
      )
    }

    <.div(^.className := "column columns",
      <.aside(^.className := "column menu",
        <.ul(^.className := "menu-list",
          menuItems.toVdomArray
        )
      ),
      <.div(^.className := "column is-four-fifths",
        props.pages(state.value.selectedKey)()
      )
    )

  }

  private val component = ScalaFnComponent.withHooks[Props]
    .useStateBy(props => State("Capital Game"))
    .render(renderFn)

  def build() = {
    val p = Props(Map(
      "Tips Calculator" -> TippsCalculator.component,
      "Income Calculator" -> IncomeCalculator.component,
      "Memory" -> Memory.component,
      "Form" -> FormExample.component,
      "Kanban" -> BoardComponent.component,
      "Complete 4" -> Complete4Board.component,
      "Capital Game" -> GuessingGameWrapper.component,
    ))

    component(p)
  }
}
