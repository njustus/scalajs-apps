package com.github.njustus.sjsapps.incomecalculator

import java.time.*

enum Interval(perYear: Int):
  case Monthly extends Interval(12)
  case Yearly extends Interval(1)

case class Income(description: String,
                  amount: BigDecimal,
                  paymentInterval: Interval,
                  since: LocalDate
                 )
object Income {
  def zero: Income = Income("", BigDecimal(0), Interval.Yearly, Instant.now.atOffset(ZoneOffset.UTC).toLocalDate)
}
