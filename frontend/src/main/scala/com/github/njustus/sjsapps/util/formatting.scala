package com.github.njustus.sjsapps.util

object formatting {
  def formatCurrency(x: BigDecimal): String =
    f"$x%.2f €"
}
