package net.cakesolutions.strictify

import net.cakesolutions.strictify.testkit.BaseBindingSpec

class CoreBindingSpec extends BaseBindingSpec {
  "Core Bindings should" - {
    "convert Option of" - {
      "Ints" in check[Int, Option[Int]](1, Some(1))
      "Strings" in check[String, Option[String]]("b", Some("b"))
    }
    "convert Some of" - {
      "Ints" in check(1, Some(1))
      "Strings" in check("b", Some("b"))
    }
    "pass the same value of" - {
      "Ints" in check(1, 1)
      "Strings" in check("b", "b")
      "Booleans" in check(false, false)
    }
  }
}