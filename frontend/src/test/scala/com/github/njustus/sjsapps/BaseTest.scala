package com.github.njustus.sjsapps

import org.scalatest.*
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

abstract class BaseTest
  extends AnyFlatSpec
  with Matchers
  with BeforeAndAfter
  with Inspectors
  with OptionValues
  with EitherValues