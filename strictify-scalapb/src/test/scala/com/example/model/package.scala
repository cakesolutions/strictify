package com.example

import net.cakesolutions.strictify.DefaultBindingConfig
import net.cakesolutions.strictify.scalapb.{BindingRefinedTypesImplicits, BindingSameScalaPbClassesImplicits}

// Our binding flavour, provides implicit instances to all classes in this package
package object model extends DefaultBindingConfig with BindingSameScalaPbClassesImplicits with BindingRefinedTypesImplicits