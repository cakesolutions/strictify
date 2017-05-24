package net.cakesolutions.strictify.scalapb

import net.cakesolutions.strictify.core._

trait StrictifyScalaPbClassesImplicits extends StrictifyInstanceHelpers {

}
trait LoosenScalaPbClassesImplicits extends LoosenInstanceHelpers {

}
trait BindingSameScalaPbClassesImplicits extends StrictifyScalaPbClassesImplicits with LoosenScalaPbClassesImplicits