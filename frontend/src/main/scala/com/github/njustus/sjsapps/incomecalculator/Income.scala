package com.github.njustus.sjsapps.incomecalculator

import java.time.*

enum Interval(val perMonth: Int):
  case Monthly extends Interval(1)
  case Yearly extends Interval(12)

case class Income(description: String,
                  amount: BigDecimal,
                  paymentInterval: Interval,
                  since: LocalDate
                 ) {
  def monthlyAmount: BigDecimal = paymentInterval match {
    case y@Interval.Yearly => amount / y.perMonth
    case m@Interval.Monthly => amount / m.perMonth
  }

  def yearlyAmount: BigDecimal = monthlyAmount * Interval.Yearly.perMonth
}

object Income {
  def zero: Income = Income("", BigDecimal(0), Interval.Yearly, Instant.now.atOffset(ZoneOffset.UTC).toLocalDate)
}
