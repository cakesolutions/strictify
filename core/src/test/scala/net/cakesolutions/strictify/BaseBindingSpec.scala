package net.cakesolutions.strictify

import net.cakesolutions.strictify.core.{ Loosen, Strictify }
import org.scalatest.{ FreeSpec, Matchers }

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
}
