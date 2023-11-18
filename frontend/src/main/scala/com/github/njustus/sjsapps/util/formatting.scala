package com.github.njustus.sjsapps.util

import org.scalajs.dom.intl.{NumberFormat, NumberFormatOptions}

import java.time.Instant
import java.time.format.{DateTimeFormatter, FormatStyle}
import scala.scalajs.js.Date

object formatting {
  private def numberFormat = new NumberFormat(
    "de-DE",
    new NumberFormatOptions {
      minimumFractionDigits = 2
      maximumFractionDigits = 2
    }
  )

  private def currencyFormat = new NumberFormat(
    "de-DE",
    new NumberFormatOptions {
      minimumFractionDigits = 2
      maximumFractionDigits = 2
      currency = "EUR"
      style = "currency"
    }
  )

  def formatNumber(x: BigDecimal): String =
    if (x == null) numberFormat.format(0.0)
    else numberFormat.format(x.doubleValue)

  def formatCurrency(x: BigDecimal): String =
    if (x == null) currencyFormat.format(0.0)
    else currencyFormat.format(x.doubleValue)

  def formatDateTime(instant: Instant): String =
    new Date(instant.toEpochMilli).toLocaleString()
}
