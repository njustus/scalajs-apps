package com.github.njustus.sjsapps.incomecalculator

import cats.effect.SyncIO
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*

import java.time.LocalDate

object IncomeForm {
  private def zero: State = Income.zero

  case class Props(addIncome: (x:Income) => SyncIO[Unit])

  type State = Income

  private def intervalOptions = Interval.values.map { interval =>
    <.option(^.value:=interval.toString,
      ^.key:=interval.toString,
      interval.toString)
  }.toVdomArray

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    def updateAmount(ev:ReactEventFromInput) =
      val value = ev.target.value
      state.modState(_.copy(amount = BigDecimal(value)))

    def updateInterval(ev:ReactEventFromInput) =
      val value = ev.target.value
      state.modState(_.copy(paymentInterval = Interval.valueOf(value)))

    def updateSinceDate(ev:ReactEventFromInput) =
      val value = ev.target.value
      state.modState(_.copy(since = LocalDate.parse(value)))

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
        ^.onClick --> props.addIncome(state.value)
      )
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .useState(zero)
    .render((p, st) => renderFn(p, st))
}
