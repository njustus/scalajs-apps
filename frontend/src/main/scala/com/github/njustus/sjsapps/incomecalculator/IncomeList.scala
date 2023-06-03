package com.github.njustus.sjsapps.incomecalculator

import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import Income.*
import java.time.LocalDate

object IncomeList {
  //TODO collapse everything besides yearly amount
  //TOOD display increase in % against previous increase
  //TODO enable testing increases 'what is 3000€ in % - what are 3%?)
  //TODO display . betweeen thousand
  case class Props(xs:List[Income]) {
    val incomeGroups: List[IncomeGroup] = xs.groupBy(i => i.since)
      .map(IncomeGroup.apply)
      .toList
      .sorted

    val incomeGroupsWithSum: List[(IncomeGroup, BigDecimal)] =
      if(incomeGroups.isEmpty)
        List.empty
      else if(incomeGroups.size==1)
        List(incomeGroups.head -> incomeGroups.head.amount)
      else
        val sums = incomeGroups.tail.scanLeft(incomeGroups.head.amount) {
          (acc, income) => acc+income.amount
        }

        val xs = incomeGroups.zip(sums).reverse
        println(xs)
        xs
  }

  case class State()

  private def renderIncome(income:Income, idx:Int) =
    <.div(^.key:=s"income-$idx", ^.className:="columns",
      <.div(^.className:="column is-three-fifths", income.description),
      //<.div(^.className:="column", s"Seit: ${income.since}"),
      <.div(^.className:="column text-align-right", formatting.formatCurrency(income.monthlyAmount)),
      <.div(^.className:="column text-align-right", <.strong(formatting.formatCurrency(income.yearlyAmount))),
    )

  private def renderIncomeGroup(incomeGroup: IncomeGroup, currentTotal:BigDecimal, idx:Int) =
    val incomeList = incomeGroup.incomes.zipWithIndex.map(renderIncome)

    <.div(^.key:= s"income-group-$idx", ^.className:="card",
      <.div(^.className:="card-header",
        <.div(^.className:="card-header-title",
          <.div(^.className:="column is-four-fifths", incomeGroup.since.toString),
          <.div(^.className := "column text-align-right",
            <.span(formatting.formatCurrency(currentTotal)),
            <.br(),
            <.small(^.className := "has-text-success", s"+${formatting.formatNumber(incomeGroup.percent(currentTotal))}%")
          ),
        )
      ),
      <.div(^.className:="card-content",
        (incomeList :+
          <.div(^.className:="columns",
            <.div(^.className:="column is-three-fifths"),
            <.div(^.className:="column text-align-right", <.small("monthly")),
            <.div(^.className:="column text-align-right", <.small("yearly")),
          )
          ).toVdomArray
      )
    )

  private def renderFn(props: Props): VdomNode = {
    <.div(
      props.incomeGroupsWithSum.zipWithIndex.map{ case ((group, total), idx) => renderIncomeGroup(group, total, idx)}.toVdomArray
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .render(p => renderFn(p))
}
