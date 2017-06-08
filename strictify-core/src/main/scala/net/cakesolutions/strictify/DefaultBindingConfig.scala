package net.cakesolutions.strictify

import net.cakesolutions.strictify.core.{
  BindingOptionsImplicits,
  BindingSeqsImplicits
}
import net.cakesolutions.strictify.structured.BindingSimilarCaseClassesImplicits

trait DefaultBindingConfig
    extends BindingSimilarCaseClassesImplicits
    with BindingOptionsImplicits
    with BindingSeqsImplicits

object DefaultBindingConfig extends DefaultBindingConfig
