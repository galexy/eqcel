package net.reasoning.eqcel.compiler

import net.reasoning.eqcel.formulas.Formulas
import net.reasoning.eqcel.formulas.RangeMetadata
import net.reasoning.eqcel.formulas.SheetExpression

import net.reasoning.eqcel.intermediate.ReifiedSheet
import net.reasoning.eqcel.intermediate.ReifiedRange
import net.reasoning.eqcel.intermediate.ReifiedExpressions._

import cats.data.State
import cats.Foldable._
import cats.instances.list._

/** Compiler phase that takes a tree of user defined formulas and reifies them.
 * 
 * A reified tree of formulas is represented as a readonly set of partial 
 * functions on range index (for a cell) to the expression for that cell.
 * A range's formula and all of its overrides are collasped into a single
 * partial function.
 */
object ReifyFormulaPhase extends Phase[Formulas, ReifiedSheet] {
  type Context = Map[Int, ReifiedRange]
  type SheetFormula = SheetExpression.IntLit => SheetExpression.Expr

  def transform(tree: Formulas): ReifiedSheet = {
    val ranges = tree.ranges map (transformRange)

    ReifiedSheet(ranges)
  }

  // TODO: figure out how to make Expr an instance of cats Traversable
  def transformRange(r: RangeMetadata) = {
    val overrides = r.overrides.map(entry => transformSheetOverride(entry._1, entry._2))
    val baseFunc = transformSheetFormula(r.baseFormula)
    val reifiedFormula = if (overrides.isEmpty) baseFunc 
                        else (overrides reduce (_ orElse _)) orElse baseFunc

    ReifiedRange(r.hashId, r.start, r.end, reifiedFormula)
  }

  def transformSheetFormula(formula: SheetFormula): PartialFunction[Int, Expr] = {
    case i => rewriteSheetExpr(formula(SheetExpression.IntLit(i)))
  }

  def transformSheetOverride(i: Int, expr: SheetExpression.Expr): PartialFunction[Int, Expr] = {
    case `i` => rewriteSheetExpr(expr)
  }

  def rewriteSheetExpr(expr: SheetExpression.Expr): Expr = expr match {
    case SheetExpression.Empty => Empty
    case SheetExpression.IntLit(i) => IntLit(i)
    case SheetExpression.Add(left, right) =>
      Add(rewriteSheetExpr(left), rewriteSheetExpr(right))
    case SheetExpression.Sub(left, right) =>
      Sub(rewriteSheetExpr(left), rewriteSheetExpr(right))
    case SheetExpression.RangeIndexExpr(range, index) => 
      RangeIndexExpr(range.hashCode, rewriteSheetExpr(index))
    case otherwise => ???
  }
}