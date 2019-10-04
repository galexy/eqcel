package net.reasoning.eqcel
package test

import org.scalatest._
import net.reasoning.eqcel.formulas._
import net.reasoning.eqcel.compiler._
import net.reasoning.eqcel.intermediate._

class DevectorPhaseSuite extends FunSuite
  with DiagrammedAssertions 
  with Matchers {
  test("empty expand phase") {
    object Model extends Sheet

    val expanded = DevectorPhase.transform(Model)
    assert(expanded == ExpandedSheet(Seq()))
  }

  test("expand sheet with empty range") {
    object Model extends Sheet {
      val range = Range[0, 2]
    }

    val expanded = DevectorPhase.transform(Model)
    expanded should matchPattern {
      case ExpandedSheet(Seq(ExpandedRange(_, Seq(EmptyCell, EmptyCell)))) =>
    }
  }

  test("expand empty range with an override") {
    object Model extends Sheet {
      val range = Range[0, 3]
      range(1) = 1
    }

    val expanded = DevectorPhase.transform(Model)
    expanded should matchPattern {
      case ExpandedSheet(Seq(ExpandedRange(_,Seq(
        EmptyCell,
        IntCell(1),
        EmptyCell
      )))) =>
    }
  }

  test("expanded formula without range references") {
    // Note: no constant expression folding
    object Model extends Sheet {
      val formula = FormulaRange[0, 3](index => index + 1)
    }

    val expanded = DevectorPhase.transform(Model)
    expanded should matchPattern {
      case ExpandedSheet(Seq(ExpandedRange(_, Seq(
        FormulaCell(OpUFExpr(Plus, IntUFExpr(1), IntUFExpr(1))),
        FormulaCell(OpUFExpr(Plus, IntUFExpr(2), IntUFExpr(1))),
        FormulaCell(OpUFExpr(Plus, IntUFExpr(3), IntUFExpr(1)))
      )))) =>
    }
  }

  test("expanded formula with range references") {
    object Model extends Sheet {
      val range = Range[0, 3]
      val formula = FormulaRange[0, 3](index => range(index) + 1)
    }

    val expanded = DevectorPhase.transform(Model)
    val rangeHashId = s"range_Model.range.hashCode"

    expanded should matchPattern {
      case ExpandedSheet(Seq(ExpandedRange(_, Seq(
        FormulaCell(OpUFExpr(Plus, IndexRangeRefUFExpr(`rangeHashId`, IntUFExpr(1)), IntUFExpr(1))),
        FormulaCell(OpUFExpr(Plus, IndexRangeRefUFExpr(`rangeHashId`, IntUFExpr(2)), IntUFExpr(1))),
        FormulaCell(OpUFExpr(Plus, IndexRangeRefUFExpr(`rangeHashId`, IntUFExpr(3)), IntUFExpr(1)))
      )))) => 
    }
  }
}
