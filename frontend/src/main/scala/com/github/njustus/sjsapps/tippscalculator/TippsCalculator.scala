package com.github.njustus.sjsapps.tippscalculator

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.window
import cats.effect._
import japgolly.scalajs.react.hooks.Hooks.UseState
import com.github.njustus.sjsapps.*

object TippsCalculator {

  case class State(totalAmount:BigDecimal,
                   tipPercent:Option[Float]) {
    def tip: Float = tipPercent.getOrElse(1)
    def tipAmount: BigDecimal = totalAmount*BigDecimal(tip)

    def tipDisplayProps = TipDisplay.Props(totalAmount, tipAmount)
  }

  object State {
    def zero: State = State(0, None)
  }

  def renderFn(state: UseState[State]): VdomNode = {
    def tipSelected(percent: Float): IO[Unit] = {
      state.modState(x => x.copy(tipPercent = Some(percent))).to[IO]
    }

    def setAmount (ev: ReactEventFromInput) = {
      val value = ev.target.value
      state.modState(x => x.copy(totalAmount = BigDecimal(value)))
    }

    <.div(^.className := "mt-6 column is-three-quarters columns is-vcentered",
      <.div(^.className := "column",
        <.div(^.className := "block",
          <.h4(^.className := "title is-4", "Bill"),
          <.input(^.className := "input is-large",
            ^.`type` := "number",
            ^.placeholder := "56.89",
            ^.onChange ==> setAmount)
        ),
        <.div(^.className := "block",
          <.h4(^.className := "title is-4", "Select Tip"),
          TippSelector.component(TippSelector.Props(state.value.tipPercent, tipSelected))
        ),
      ),
      <.div(^.className := "column",
        TipDisplay.component(state.value.tipDisplayProps)
      )
    )

  }

  val component: PageComponent = ScalaFnComponent.withHooks[Unit]
    .useState(State.zero)
    .render { (_, state) =>
      renderFn(state)
    }
}
