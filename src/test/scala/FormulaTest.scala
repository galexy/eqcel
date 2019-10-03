import org.scalatest._
import net.reasoning.eqcel.formulas._

class ExprSuite extends FunSuite with DiagrammedAssertions with Matchers {

  test("define a range with a formula referencing another range cell") {
    object Model extends Sheet {
      val source = Range()
      val range: FormulaRange = (index: Expr) => source(index - 1)
    }

    Model.range.formula(100) should matchPattern {
      case RangeIndexExpr(_, Sub(IntLit(100), IntLit(1))) =>
    }
  }

  test("creating an formula constant override") {
    object Model extends Sheet {
      val rangeWithOverride = Range()
      rangeWithOverride(2000) = 1
    }

    assert(IntLit(1) == Model.rangeWithOverride.formula(2000))
  }

  test("expands function calls") {
    object Model extends Sheet {
      def inc(i: Expr) = i + 1

      val range = FormulaRange(index => inc(index))
    }

    Model.range.formula(100) should matchPattern {
      case Add(IntLit(100), IntLit(1)) =>
    }
  }

  test("define a range recusrively") {
    object Model extends Sheet {
      val revenue = Range()
      val cumuRevenue: FormulaRange = 
        (year: Expr) => cumuRevenue(year -1) + revenue(year)        
    }

    Model.cumuRevenue.formula(2001) should matchPattern {
      case Add(RangeIndexExpr(FormulaRange(_), Sub(IntLit(2001), IntLit(1))), _) =>
    }
  }

}