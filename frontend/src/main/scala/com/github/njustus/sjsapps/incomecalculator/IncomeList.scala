package com.github.njustus.sjsapps.incomecalculator

import com.github.njustus.sjsapps.util.formatting
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._

object IncomeList {
  //TODO group by date
  //TODO collapse everything besides yearly amount
  //TOOD display increase in % against previous increase
  //TODO enable testing increases 'what is 3000€ in % - what are 3%?)
  case class Props(xs:List[Income])

  case class State()

  private def renderIncome(income:Income, idx:Int) =
    <.div(^.key:=s"key-$idx", ^.className:="box columns",
      <.div(^.className:="column", income.description),
      <.div(^.className:="column", s"Seit: ${income.since}"),
      <.div(^.className:="column", formatting.formatCurrency(income.monthlyAmount)),
      <.div(^.className:="column", <.strong(formatting.formatCurrency(income.yearlyAmount))),
    )

  private def renderFn(props: Props): VdomNode = {
    <.div(
      props.xs.zipWithIndex.map(renderIncome).toVdomArray
    )
  }

  val component = ScalaFnComponent.withHooks[Props]
    .render(p => renderFn(p))
}
