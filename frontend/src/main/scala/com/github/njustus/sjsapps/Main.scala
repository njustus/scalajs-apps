package com.github.njustus.sjsapps

import com.github.njustus.sjsapps.tippscalculator.TippsCalculator
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.document

import java.time._

object Main {
  def main(args: Array[String]): Unit = {
    val root = document.getElementById("parent-root")
    <.div(
      ^.className := "columns is-centered",
      TippsCalculator.component()
    ).renderIntoDOM(root)
  }
}
