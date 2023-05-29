package com.github.njustus

import japgolly.scalajs.react.{CtorType, ScalaFnComponent}

package object sjsapps {
  type PageComponent = ScalaFnComponent[Unit, CtorType.Nullary]
}
