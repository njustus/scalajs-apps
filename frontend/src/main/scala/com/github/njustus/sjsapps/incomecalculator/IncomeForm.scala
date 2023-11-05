package com.github.njustus.sjsapps.incomecalculator

import cats.effect.SyncIO
import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*

import java.time.LocalDate
import scala.util.{Try, Using}

object IncomeForm {
  private def zero: State = Income.zero

  case class Props(currentTotalIncome: BigDecimal, addIncome: (x: Income) => SyncIO[Unit])

  type State = Income

  private def intervalOptions = Interval.values.map { interval =>
    <.option(^.value := interval.toString, ^.key := interval.toString, interval.toString)
  }.toVdomArray

  private def increaseInPercent(props: Props, state: State): BigDecimal =
    (state.yearlyAmount / props.currentTotalIncome) * 100

  private def renderFn(props: Props, state: Hooks.UseState[State]): VdomNode = {
    println(s"current total ${props.currentTotalIncome}")
    def updateAmount(ev: ReactEventFromInput) =
      val value      = ev.target.value
      val bigDecimal = Try { BigDecimal(value) }.getOrElse(BigDecimal(0.0))
      state.modState(_.copy(amount = bigDecimal))

    def updateDescription(ev: ReactEventFromInput) =
      val value = ev.target.value
      state.modState(_.copy(description = value))

    def updateInterval(ev: ReactEventFromInput) =
      val value = ev.target.value
      state.modState(_.copy(paymentInterval = Interval.valueOf(value)))

    def updateSinceDate(ev: ReactEventFromInput) =
      val value = ev.target.value
      val date  = Try { LocalDate.parse(value) }.getOrElse(LocalDate.now())
      state.modState(_.copy(since = date))

    <.div(
      ^.className := "columns",
      <.input(^.className := "column input", ^.placeholder := "Description", ^.onChange ==> updateDescription),
      <.input(^.className := "column input", ^.placeholder := "Amount", ^.onChange ==> updateAmount),
      <.div(
        ^.className := "select",
        <.select(
          intervalOptions,
          ^.onChange ==> updateInterval
        )
      ),
      <.input(^.className := "column input", ^.`type` := "date", ^.onChange ==> updateSinceDate),
      <.div(
        ^.className := "column has-text-success",
        s"+${formatting.formatNumber(increaseInPercent(props, state.value))}%"
      ),
      <.button(^.className := "button is-primary", "Hinzufügen", ^.onClick --> props.addIncome(state.value))
    )
  }

  val component = ScalaFnComponent
    .withHooks[Props]
    .useState(zero)
    .render((p, st) => renderFn(p, st))
}
