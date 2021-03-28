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
    object Sheet extends Worksheet

    val expanded = new Compiler().compile(Sheet)
    assert(expanded == ExpandedSheet(Seq()))
  }

  test("expand sheet with empty range") {
    object Model extends Worksheet with Formulas {
      val range = Range[0, 2]

      place row range at A(1)
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

    object Sheet extends Worksheet {
      place row Model.range at A(1)
    }

    val expanded = new Compiler().compile(Sheet)
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

    object Sheet extends Worksheet {
      place row Model.formula at A(1)
    }

    val expanded = new Compiler().compile(Sheet)
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

    object Sheet extends Worksheet {
      place row Model.range at A(1)
      place row Model.formula at A(2)
    }

    val expanded = new Compiler().compile(Sheet)
    val rangeHashId = s"range_Model.range.hashCode"

    expanded should matchPattern {
      case ExpandedSheet(Seq(
        ExpandedRange(_, _),
        ExpandedRange(_, Seq(
          FormulaCell("=(A1+1)"),
          FormulaCell("=(B1+1)"),
          FormulaCell("=(C1+1)"),
        )
      ))) => 
    }
  }
}