package net.cakesolutions.strictify.core

/**
  * Binds a Strict type (usually the model classes) to a loose version of it
  */
trait Binding[Strict] {
  type Loose
  def strictify: Strictify[Strict, Loose]
  def loosen: Loosen[Strict, Loose]
}
object Binding {
  type Aux[S, L] = Binding[S] { type Loose = L }

  def instance[S, L](implicit s: Strictify[S, L], l: Loosen[S, L]): Aux[S, L] =
    new Binding[S] {
      override type Loose = L
      override def strictify = s
      override def loosen    = l
    }
}
