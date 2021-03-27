package net.reasoning.eqcel
package test

import org.scalatest._
import net.reasoning.eqcel.formulas._
import net.reasoning.eqcel.compiler._
import net.reasoning.eqcel.intermediate._

class CompilerSuite extends FunSuite 
  with DiagrammedAssertions 
  with Matchers 
  with Inside {
   
  test("empty expand phase") {
    object Model extends Formulas

    val expanded = new Compiler().compile(Model)
    assert(expanded == ExpandedSheet(Seq()))
  }

  test("expand sheet with empty range") {
    object Model extends Formulas {
      val range = Range[0, 2]
    }

    val expanded = new Compiler().compile(Model)
    expanded should matchPattern {
      case ExpandedSheet(Seq(ExpandedRange("A1:C1", Seq(EmptyCell, EmptyCell, EmptyCell)))) =>
    }
  }

  test("expand empty range with an override") {
    object Model extends Formulas {
      val range = Range[0, 2]
      range(1) = 1
    }

    val expanded = new Compiler().compile(Model)
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
    object Model extends Formulas {
      val formula = FormulaRange[0, 2](index => index + 1)
    }

    val expanded = new Compiler().compile(Model)
    expanded should matchPattern {
      case ExpandedSheet(Seq(ExpandedRange(_, Seq(
        FormulaCell("=(0+1)"),
        FormulaCell("=(1+1)"),
        FormulaCell("=(2+1)")
      )))) =>
    }
  }

  test("expanded formula with range references") {
    object Model extends Formulas {
      val range = Range[0, 2]
      val formula = FormulaRange[0, 2](index => range(index) + 1)
    }

    val expanded = new Compiler().compile(Model)
    val rangeHashId = s"range_Model.range.hashCode"

    // NOTE: currently, the layout is non-deterministic
    expanded should (matchPattern {
      case ExpandedSheet(Seq(
        ExpandedRange(_, _),
        ExpandedRange(_, Seq(
          FormulaCell("=(A1+1)"),
          FormulaCell("=(B1+1)"),
          FormulaCell("=(C1+1)"),
        )
      ))) => 
    } or matchPattern {
      case ExpandedSheet(Seq(
        ExpandedRange(_, Seq(
          FormulaCell("=(A2+1)"),
          FormulaCell("=(B2+1)"),
          FormulaCell("=(C2+1)"),
        )),
        ExpandedRange(_, _),
      )) => 
    })
  }
}