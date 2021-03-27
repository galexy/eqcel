package net.reasoning.eqcel
package test

import org.scalatest._
import net.reasoning.eqcel.formulas._
import net.reasoning.eqcel.compiler._
import net.reasoning.eqcel.intermediate._

class LayoutSuite extends FunSuite   
  with DiagrammedAssertions 
  with Matchers 
  with Inside {

  test("layout a row range") {
    object Model extends Formulas {
      val range = Range[0, 2]
    }

    object Foo extends Worksheet {
      place row Model.range at A(1)
    }
  }

  test("combine rows and then layout") {
    object Model extends Formulas {
      val range = Range[0, 2]
      val range2 = Range[0, 2]
    }

    object Foo extends Worksheet {
      val table = Model.range ++ Model.range2

      place(table) at A(1)
    }
  }

  test("combine rows and then layout") {
    object Model extends Formulas {
      val range = Range[0, 2]
      val range2 = Range[0, 2]
    }

    object Foo extends Worksheet {
      val table = Model.range || Model.range2

      place(table) at A(1)
    }
  }

}
