package net.reasoning.eqcel.compiler
import net.reasoning.eqcel.intermediate.ExpandedSheet
import net.reasoning.eqcel.intermediate.Cell
import net.reasoning.eqcel.intermediate.LayoutSheet
import net.reasoning.eqcel.intermediate.ExpandedRange
import net.reasoning.eqcel.intermediate.RangeLayouts
import net.reasoning.eqcel.intermediate.ReifiedRange
import net.reasoning.eqcel.intermediate.ReifiedExpressions._
import net.reasoning.eqcel.intermediate.EmptyCell
import net.reasoning.eqcel.intermediate.IntCell
import net.reasoning.eqcel.intermediate.FormulaCell

/** Compiler phase that expands ranges into individual cells. 
  * 
  */
object FlattenPhase extends Phase[LayoutSheet, ExpandedSheet] {
  def transform(tree: LayoutSheet): ExpandedSheet = {
    val expandedRanges = tree.rangesDefintions.map { r =>
      val rangeLayout = tree.rangeLocations.layouts(r.id)

      ExpandedRange(rangeLayout.fullAddress, expandCells(r)(tree.rangeLocations))
    }

    ExpandedSheet(expandedRanges)
  }

  def expandCells(range: ReifiedRange)(implicit locations: RangeLayouts): Seq[Cell] = {
      for {
        index <- range.start to range.end
      } yield transformExprCell(range.definition(index))
  }

  def transformExprCell(expr: Expr)(implicit locations: RangeLayouts): Cell = expr match {
    case Empty => EmptyCell
    case IntLit(value) => IntCell(value)
    case expr => FormulaCell("=" + transformExpr(expr))
  }

  def transformExpr(expr: Expr)(implicit locations: RangeLayouts): String = expr match {
    case Empty => ??? // If we got here, we have a bug
    case IntLit(value) => value.toString()
    case Add(left, right) => s"(${transformExpr(left)}+${transformExpr(right)})"
    case Sub(left, right) => s"(${transformExpr(left)}-${transformExpr(right)})"
    case RangeIndexExpr(range, index) => {
      val rangeLocation = locations.layouts(range)
      rangeLocation.reference(evalExpr(index))
    }
  }

  def evalExpr(expr: Expr): Int = expr match {
    case Empty => ???
    case RangeIndexExpr(range, index) => ???  // not supported, and we should fail at a sematnic check phase
    case Add(left, right) => evalExpr(left) + evalExpr(right)
    case Sub(left, right) => evalExpr(left) - evalExpr(right)
    case IntLit(value) => value
  }
}