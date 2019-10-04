package net.reasoning.eqcel.intermediate

import net.reasoning.eqcel.expressions._
import net.reasoning.eqcel.formulas._

object RefiedExpressions extends ExprModule {
  case class RangeIndexExpr(
    range: ReifiedRange,
    index: Expr
  ) extends GenRangeIndexExpr[ReifiedRange]

  case class UnresolvedIndexExpr(
    range: String,
    index: Expr
  ) extends GenRangeIndexExpr[String]
}

import RefiedExpressions._

case class ReifiedSheet(ranges: Seq[ReifiedRange])
case class ReifiedRange(start: Int, end: Int, definition: PartialFunction[Int, Expr])