package com.github.njustus.sjsapps.tippscalculator

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react.hooks.Hooks.UseState
import org.scalajs.dom.intl.NumberFormatOptions

object TipDisplay {
  case class Props(amount: BigDecimal,
                   tipAmount: BigDecimal)

  object Props {
    def zero: Props = Props(0, 0)
  }

  def renderFn(props: Props): VdomNode = {
    <.div(
    <.h3(^.className := "title is-3 has-text-info",
      <.div(^.className := "columns",
        <.div(^.className := "column", "Amount"),
        <.div(^.className := "column", formatting.formatCurrency(props.amount))
      )),
      <.h2(^.className := "title is-2 has-text-success",
      <.div(^.className := "columns",
        <.div(^.className := "column", "Tip"),
        <.div(^.className := "column has-text-weight-bold", formatting.formatCurrency(props.tipAmount))
      )
    ))
  }

  val component = ScalaFnComponent.withHooks[Props]
    .render { (props) =>
      renderFn(props)
    }
}
