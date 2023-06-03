package com.github.njustus.sjsapps.util

import org.scalajs.dom.intl.{NumberFormat, NumberFormatOptions}

object formatting {
  private def numberFormat = new NumberFormat("de-DE", new NumberFormatOptions {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
  })

  private def currencyFormat = new NumberFormat("de-DE", new NumberFormatOptions {
    minimumFractionDigits = 2
    maximumFractionDigits = 2
    currency = "EUR"
    style = "currency"
  })

  def formatNumber(x: BigDecimal): String =
    if(x == null) numberFormat.format(0.0)
    else numberFormat.format(x.doubleValue)


  def formatCurrency(x: BigDecimal): String =
    if(x == null) currencyFormat.format(0.0)
    else currencyFormat.format(x.doubleValue)
}
