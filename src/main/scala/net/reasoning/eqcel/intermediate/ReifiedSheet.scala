package net.reasoning.eqcel.intermediate

import net.reasoning.eqcel.expressions._
import net.reasoning.eqcel.formulas._

object ReifiedExpressions extends ExprModule {
  case class RangeIndexExpr(
    range: Int,
    index: Expr
  ) extends GenRangeIndexExpr[Int]

  case class Var(name: String) extends Expr
}

import ReifiedExpressions._

case class ReifiedSheet(ranges: Seq[ReifiedRange])
case class ReifiedRange(id: Int, start: Int, end: Int, definition: Int => Expr)