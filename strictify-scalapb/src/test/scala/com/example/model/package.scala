package com.example

import net.cakesolutions.strictify.DefaultBindingConfig
import net.cakesolutions.strictify.scalapb.{BindingSameRefinedTypesImplicits, StrictifyScalaPbClassesImplicits}
import net.cakesolutions.strictify.structured.BindingSimilarCaseClassesImplicits

// Our binding flavour, provides implicit instances to all classes in this package
package object model extends DefaultBindingConfig with BindingSimilarCaseClassesImplicits
  with StrictifyScalaPbClassesImplicits with BindingSameRefinedTypesImplicits