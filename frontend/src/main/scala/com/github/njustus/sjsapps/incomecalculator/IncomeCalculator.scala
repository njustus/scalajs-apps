package com.github.njustus.sjsapps.incomecalculator

import com.github.njustus.sjsapps.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import monocle.Focus
import cats.effect.IO

import java.time.LocalDate

object IncomeCalculator {

  val dummyIncomes = List(
    Income("Grundgehalt", BigDecimal(2500), Interval.Monthly, LocalDate.of(2015,3,1)),
    Income("Bonus", BigDecimal(2000), Interval.Yearly, LocalDate.of(2015,3,1)),
    Income("T&M", BigDecimal(300), Interval.Monthly, LocalDate.of(2015,5,1))
  )

  def addIncome(x:Income) = Focus[State](_.incomes).modify(xs => x::xs)
  case class State(incomes:List[Income])

  private def renderFn(state: Hooks.UseState[State]): VdomNode = {
    val addIncomeState = (x:Income) => state.modState(addIncome(x))

    <.div(^.className:="mt-4 columns is-multiline",
      <.div(^.className:="column is-full",
        IncomeForm.component(IncomeForm.Props(addIncomeState))
      ),
      <.div(^.className:="mt-2 column is-full",
        IncomeList.component(IncomeList.Props(state.value.incomes))
      )
    )
  }

  val component: PageComponent = ScalaFnComponent.withHooks[Unit]
    .useStateBy(props => State(dummyIncomes))
    .render((_, state) => renderFn(state))
}
