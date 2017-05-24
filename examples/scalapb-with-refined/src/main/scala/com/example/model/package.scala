package com.example

import net.cakesolutions.strictify.DefaultBindingConfig
import net.cakesolutions.strictify.scalapb.BindingSameRefinedTypesImplicits
import net.cakesolutions.strictify.structured.{BindingSimilarCaseClassesImplicits, BindingSimilarSealedTraitsImplicits}

// Our binding flavour, provides implicit instances to all classes in this package
package object model extends DefaultBindingConfig with BindingSimilarCaseClassesImplicits
  with BindingSimilarSealedTraitsImplicits with BindingSameRefinedTypesImplicits