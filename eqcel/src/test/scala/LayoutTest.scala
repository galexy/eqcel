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

  val compiler = (ReifyFormulaPhase.transform _) andThen
                 (LayoutPhase.transform _)

  test("layout a row range") {
    object Model extends Formulas {
      val range = Range[0, 2]
    }
    
    object Foo extends Worksheet {
      place row Model.range at A(1)
    }

    val layout = compiler(Foo)
    layout.rangesDefintions.length should equal(layout.rangeLocations.layouts.size)

    val location = layout.rangeLocations.layouts(layout.rangesDefintions(0).id)
    location.fullAddress should equal("A1:C1")
  }

  ignore("combine rows and then layout") {
    object Model extends Formulas {
      val range = Range[0, 2]
      val range2 = Range[0, 2]
    }

    object Foo extends Worksheet {
      val table = Model.range ++ Model.range2
      place(table) at A(1)
    }
  }

  ignore("combine columns and then layout") {
    object Model extends Formulas {
      val range = Range[0, 2]
      val range2 = Range[0, 2]
    }

    object Foo extends Worksheet {
      val table = Model.range || Model.range2
      place(table) at A(1)
    }
  }

  ignore("layout the same range more than once should fail") {

  }

  ignore("layout a range that is part of another range should fail") {
    
  }
}
