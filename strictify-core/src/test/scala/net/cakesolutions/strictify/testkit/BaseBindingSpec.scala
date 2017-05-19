package net.cakesolutions.strictify.testkit

import net.cakesolutions.strictify.core.{Binding, Loosen, Strictify}
import org.scalatest.{FreeSpec, Matchers}

trait BaseBindingSpec extends FreeSpec with Matchers {
  def check[S, L](
    strict: S,
    loose: L
  )(implicit strictify: Strictify[S, L], loosen: Loosen[S, L]) = {
    loose shouldBe loosen(strict)
    Right(strict) shouldBe strictify(loose)
    Right(loose) shouldBe strictify(loose).map(loosen.apply)
    Right(strict) shouldBe strictify(loosen(strict))
  }

  def checkBinding[S, L](strict: S, loose: L)(
    implicit binding: Binding.Aux[S, L]
  ) = check(strict, loose)(binding.strictify, binding.loosen)
}
