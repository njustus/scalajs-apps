package com.github.njustus.sjsapps

import com.github.njustus.sjsapps.incomecalculator.IncomeCalculator
import com.github.njustus.sjsapps.tippscalculator.TippsCalculator
import com.github.njustus.sjsapps.memory.Memory
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.*
import japgolly.scalajs.react.vdom.html_<^.*
import cats.effect.IO
import cats.effect.SyncIO

object FormExample {
  case class State(name:String, email:String, phone:String)


  private def field(name:String, value:String)(onChange: String => SyncIO[Unit]): VdomNode =
    <.div(^.className:="field",
      <.label(^.className:="label", name),
      <.div(^.className:="control",
        <.input.text(^.className:="input",
          ^.value := value,
          ^.onChange ==> ((ev:ReactEventFromInput) => {
            val v = ev.target.value
            onChange(v)
          })
        )
      )
    )

  private def renderFn(state: Hooks.UseState[State]): VdomNode = {
    <.div(^.className:="mt-4",
      <.div(^.className:="columns", <.div(^.className:="column",
        field("name", state.value.name) { value => state.modState(_.copy(name=value)) }
      )),
      <.div(^.className:="columns", <.div(^.className:="column",
        field("email", state.value.email) { value => state.modState(_.copy(email=value)) }
      )),
      <.div(^.className:="columns", <.div(^.className:="column",
        field("phone", state.value.phone) { value => state.modState(_.copy(phone=value)) }
      ))
    )
  }



  val component = ScalaFnComponent.withHooks[Unit]
    .useStateBy(props => State("", "", ""))
    .render((_, state) => renderFn(state))
}
