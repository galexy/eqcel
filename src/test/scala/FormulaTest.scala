package net.reasoning.eqcel
package test

import org.scalatest._
import net.reasoning.eqcel.formulas._

class FormulaSuite extends FunSuite 
  with DiagrammedAssertions 
  with Matchers 
  with Inside {

  test("define a range with a formula referencing another range cell") {
    object Model extends Sheet {
      val source = Range[0,10]
      val range: FormulaRange[0, 10] = (index: Expr) => source(index - 1)
    }

    Model.range.formula(IntLit(1)) should matchPattern {
      case RangeIndexExpr(_, Sub(IntLit(1), IntLit(1))) =>
    }
  }

  test("define a range with + operator") {
    object Model extends Sheet {
      val range = FormulaRange[0, 10](index => index + 100)
    }

    Model.range.formula(IntLit(1)) should matchPattern {
      case Add(IntLit(1), IntLit(100)) =>
    }
  }

  test("define a range with - operator") {
    object Model extends Sheet {
      val range = FormulaRange[0, 10](index => index - 100)
    }

    Model.range.formula(IntLit(1)) should matchPattern {
      case Sub(IntLit(1), IntLit(100)) =>
    }
  }

  test("creating an formula constant override") {
    object Model extends Sheet {
      val rangeWithOverride = Range[0, 10]
      rangeWithOverride(1) = 1
    }

    assert(IntLit(1) == Model.rangeWithOverride.formula(IntLit(1)))
  }

  test("expands function calls") {
    object Model extends Sheet {
      def inc(i: Expr) = i + 1

      val range = FormulaRange[0, 10](index => inc(index))
    }

    Model.range.formula(IntLit(1)) should matchPattern {
      case Add(IntLit(1), IntLit(1)) =>
    }
  }

  test("define a range recusrively") {
    object Model extends Sheet {
      val revenue = Range[0,10]

      val cumuRevenue: FormulaRange[2000, 2018] = 
        (year: Expr) => cumuRevenue(year -1) + revenue(year)
    }

    Model.cumuRevenue.formula(IntLit(2001)) should matchPattern {
      case Add(RangeIndexExpr(FormulaRange(_), Sub(IntLit(2001), IntLit(1))), _) =>
    }
  }

  test("index out of range for update") {
    object Model extends Sheet {
      val revenue = Range[0, 10]()
      revenue(100) = 10
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

  test("ranges have unique hashCodes") {
    object Model extends Sheet {
      val range1 = Range[0, 10]
      val range2 = Range[0, 10]

      val f1 = FormulaRange[0, 10] { i => range1(i) }
      val f2 = FormulaRange[0, 10] { i => range1(i) }

      def func(i: Expr) = range1(i)
      val f3 = FormulaRange[0, 10] (func _)
      val f4 = FormulaRange[0, 10] (func _)
    }

    assert(Model.range1.hashCode != Model.range2.hashCode)
    assert(Model.f1.hashCode != Model.f2.hashCode)
    assert(Model.f3.hashCode != Model.f4.hashCode)
  }

  test("empty range is registered with sheet") {
    object Model extends Sheet {
      val revenue = Range[0, 10]
    }
    assert(Model.ranges.length == 1)
    assert(Model.ranges(0).baseFormula(IntLit(1)) == Empty)
  }

  test("formula range is registered with sheet") {
    object Model extends Sheet {
      val f = FormulaRange[0, 10] { year => year + 1 }
    }
    assert(Model.ranges.length == 1)
    assert(Model.ranges(0).baseFormula(IntLit(0)) == Add(IntLit(0), IntLit(1)))
  }

  test("override of empty range") {
    object Model extends Sheet {
      val revenue = Range[0, 10]
      revenue(0) = 1000
    }
    assert(Model.ranges.length == 1)
    inside(Model.ranges(0)) { case RangeMetadata(_, _, _, baseFormula, overrides) => 
      assert(baseFormula(IntLit(1)) == Empty)
      assert(overrides.size == 1)
      assert(overrides(0) == IntLit(1000))
    }
  }

}