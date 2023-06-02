package com.github.njustus.sjsapps.util

object formatting {
  def formatNumber(x: BigDecimal): String = f"$x%.2f"

  def formatCurrency(x: BigDecimal): String = f"$x%.2f €"
}
