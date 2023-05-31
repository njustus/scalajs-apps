package com.github.njustus.sjsapps.incomecalculator

import cats.effect.IO
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*

import java.time.LocalDate

object IncomeForm {
  private def zero: State = Income.zero

  case class Props()

  type State = Income

  private def intervalOptions = Interval.values.map { interval =>
    <.option(^.value:=interval.toString,
      ^.key:=interval.toString,
      interval.toString)
  }.toVdomArray

  private def renderFn(state: Hooks.UseState[State]): VdomNode = {
    def updateAmount(ev:ReactEventFromInput) =
      val value = ev.target.value
      state.modState(_.copy(amount = BigDecimal(value)))

    def updateInterval(ev:ReactEventFromInput) =
      val value = ev.target.value
      state.modState(_.copy(paymentInterval = Interval.valueOf(value)))

    def updateSinceDate(ev:ReactEventFromInput) =
      val value = ev.target.value
      state.modState(_.copy(since = LocalDate.parse(value)))

    def addIncome = IO.println(state.value) //TODO zu global state hinzufügen

    <.div(^.className:="columns",
      <.input(^.className:="column is-one-quarter input", ^.placeholder:="Amount", ^.onChange ==> updateAmount),
      <.div(^.className:="select",
        <.select(
          intervalOptions,
          ^.onChange ==> updateInterval
        )
      ),
      <.input(^.className:="column is-one-quarter input",
        ^.`type`:="date",
        ^.onChange ==> updateSinceDate),
      <.button(^.className:="button is-primary",
        "Hinzufügen",
        ^.onClick --> addIncome
      )
    )
  }

  val component = ScalaFnComponent.withHooks[Unit]
    .useState(zero)
    .render((_, st) => renderFn(st))
}
