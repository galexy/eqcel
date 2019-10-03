package net.reasoning.eqcel
package test

import org.scalatest._
import net.reasoning.eqcel.formulas._

class ExprSuite extends FunSuite with DiagrammedAssertions with Matchers {

  test("define a range with a formula referencing another range cell") {
    object Model extends Sheet {
      val source = Range[0,10]
      val range: FormulaRange[0, 10] = (index: Expr) => source(index - 1)
    }

    Model.range.formula(1) should matchPattern {
      case RangeIndexExpr(_, Sub(IntLit(1), IntLit(1))) =>
    }
  }

  test("creating an formula constant override") {
    object Model extends Sheet {
      val rangeWithOverride = Range[0, 10]
      rangeWithOverride(1) = 1
    }

    assert(IntLit(1) == Model.rangeWithOverride.formula(1))
  }

  test("expands function calls") {
    object Model extends Sheet {
      def inc(i: Expr) = i + 1

      val range = FormulaRange[0, 10](index => inc(index))
    }

    Model.range.formula(1) should matchPattern {
      case Add(IntLit(1), IntLit(1)) =>
    }
  }

  test("define a range recusrively") {
    object Model extends Sheet {
      val revenue = Range[0,10]

      val cumuRevenue: FormulaRange[2000, 2018] = 
        (year: Expr) => cumuRevenue(year -1) + revenue(year)        
    }

    Model.cumuRevenue.formula(2001) should matchPattern {
      case Add(RangeIndexExpr(FormulaRange(_), Sub(IntLit(2001), IntLit(1))), _) =>
    }
  }

  test("index out of range for update") {
    object Model extends Sheet {
      val revenue = Range[0, 10]()
      revenue.update(100, 10)
    }

    assertThrows[IllegalArgumentException](Model)
  }

  test("overrides cannot be overridden") {
    object Model extends Sheet {
      val revenue = Range[0, 10]
      revenue(1) = 0
      revenue(1) = 2
    }

    assertThrows[IllegalArgumentException](Model)
  }

}