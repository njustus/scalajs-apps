package com.github.njustus.sjsapps.tippscalculator

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import japgolly.scalajs.react.hooks.Hooks.UseState

object TipDisplay {
  case class Props(amount: BigDecimal,
                   tipAmount: BigDecimal)

  object Props {
    def zero: Props = Props(0, 0)
  }

  def renderFn(props: Props): VdomNode = {
    //TODO correctly format currencies
    <.h2(
      ^.className := "title is-2",
      <.div(^.className := "columns",
        <.div(^.className := "column", "Amount"),
        <.div(^.className := "column", props.amount.toString())
      ),
      <.div(^.className := "columns",
        <.div(^.className := "column", "Tip"),
        <.div(^.className := "column", props.tipAmount.toString())
      )
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .render { (props) =>
      renderFn(props)
    }
}
