package net.reasoning.eqcel
package test

import org.scalatest._
import net.reasoning.eqcel.formulas._
import net.reasoning.eqcel.compiler._
import net.reasoning.eqcel.intermediate._

class ReifyPhaseSuite extends FunSuite
  with DiagrammedAssertions 
  with Matchers {

    import ReifiedExpressions._

    val compiler = (ReifyFormulaPhase transform _)

    test("reify empty model") {
      object Model extends Sheet
      assert(compiler(Model) == ReifiedSheet(Seq()))
    }

    test("reify range of empty cells") {
      object Model extends Sheet {
        val range = Range[0, 10]
      }
      val tree = compiler(Model)
      tree should matchPattern {
        case ReifiedSheet(Seq(ReifiedRange(_, 0, 10, _))) =>
      }

      // TODO: consider adding property tests 
      assert(tree.ranges(0).definition(0) == Empty)
    }

    test("reify an override") {
      object Model extends Sheet {
        val range = Range[0, 10]
        range(0) = 1
        range(1) = 2
      }
      val tree = compiler(Model)
      tree should matchPattern {
        case ReifiedSheet(Seq(ReifiedRange(_, 0, 10, _))) =>
      }

      // add property tests for defined at
      assert(tree.ranges(0).definition(0) == IntLit(1))
      assert(tree.ranges(0).definition(1) == IntLit(2))
      assert(tree.ranges(0).definition(2) == Empty)
    }

    test("reifying formula with plus operator") {
      object Model extends Sheet {
        val range = FormulaRange[0,10](year => year + 1)
      }
      val tree = compiler(Model)
      tree should matchPattern {
        case ReifiedSheet(Seq(ReifiedRange(_, 0, 10, _))) =>
      }

      assert(tree.ranges(0).definition(0) == Add(IntLit(0), IntLit(1)))
    }

    test("reifying formula with minux operator") {
      object Model extends Sheet {
        val range = FormulaRange[0,10](year => year - 1)
      }
      val tree = compiler(Model)
      tree should matchPattern {
        case ReifiedSheet(Seq(ReifiedRange(_, 0, 10, _))) =>
      }

      assert(tree.ranges(0).definition(0) == Sub(IntLit(0), IntLit(1)))
    }

    test("reify formula with reference") {
      object Model extends Sheet {
        val range = Range[0, 10]
        val formula = FormulaRange[0,10](index => range(index))
      }

      val tree = compiler(Model)

      // TODO: find a better way to reference ranges
      val rangeid = Model.formula.hashCode
      val formula = tree.ranges find (_.id == rangeid)
      formula.get.definition(0) should matchPattern { case RangeIndexExpr(_, IntLit(0)) => }
    }
  }

