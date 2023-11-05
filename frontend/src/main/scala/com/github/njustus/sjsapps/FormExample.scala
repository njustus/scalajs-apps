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
  enum Validated(val cssClass: String, val message: Option[String]) {
    case Valid              extends Validated("is-success", None)
    case Invalid(m: String) extends Validated("is-danger", Some(m))
    case Unknown            extends Validated("", None)
  }

  case class State(name: String, email: String, phone: String) {
    def isValid: Boolean = List(
      nameValidator(name),
      emailValidator(email),
      phoneValidator(phone)
    ).forall {
      case FormExample.Validated.Valid => true
      case _                           => false
    }
  }

  private def nameValidator(name: String): FormExample.Validated =
    if (name.isBlank) FormExample.Validated.Unknown
    else if (name.length < 2) FormExample.Validated.Invalid("must be at least 2 characters")
    else FormExample.Validated.Valid

  private def emailValidator(email: String): FormExample.Validated =
    if (email.isBlank) FormExample.Validated.Unknown
    else if (!email.contains("@")) FormExample.Validated.Invalid("must contain an @")
    else FormExample.Validated.Valid

  private def phoneValidator(phone: String): FormExample.Validated =
    if (phone.isBlank) FormExample.Validated.Unknown
    else if (!phone.matches("\\d+")) FormExample.Validated.Invalid("can contain only digits")
    else FormExample.Validated.Valid

  private def field(name: String, value: String)(
      validate: String => FormExample.Validated,
      onChange: String => SyncIO[Unit]
  ): VdomNode =
    val validated = validate(value)

    <.div(
      ^.className := "field",
      <.label(^.className := "label", name),
      <.div(
        ^.className := "control",
        <.input.text(
          ^.className := s"input ${validated.cssClass}",
          ^.value     := value,
          ^.onChange ==> ((ev: ReactEventFromInput) => {
            val v = ev.target.value
            onChange(v)
          })
        ),
        <.p(^.className := s"${validated.cssClass}", validated.message.getOrElse(""))
      )
    )

  private def renderFn(state: Hooks.UseState[State]): VdomNode = {
    <.div(
      ^.className := "mt-4",
      <.div(
        ^.className := "columns",
        <.div(
          ^.className := "column",
          field("name", state.value.name)(nameValidator, value => state.modState(_.copy(name = value)))
        )
      ),
      <.div(
        ^.className := "columns",
        <.div(
          ^.className := "column",
          field("email", state.value.email)(emailValidator, value => state.modState(_.copy(email = value)))
        )
      ),
      <.div(
        ^.className := "columns",
        <.div(
          ^.className := "column",
          field("phone", state.value.phone)(phoneValidator, value => state.modState(_.copy(phone = value)))
        )
      ),
      <.div(
        ^.className := "columns",
        <.div(^.className    := "column is-three-quarters"),
        <.button(^.className := "column button is-primary", ^.disabled := !state.value.isValid, "Submit")
      )
    )
  }

  val component = ScalaFnComponent
    .withHooks[Unit]
    .useStateBy(props => State("", "", ""))
    .render((_, state) => renderFn(state))
}
