package net.reasoning.eqcel.formulas

import net.reasoning.eqcel.expressions._

object SheetExpression extends ExprModule {
  case class RangeIndexExpr[S <: Singleton with Int, E <: Singleton with Int](
    range: Range[S,E],
    index: Expr
  )
  extends GenRangeIndexExpr[Range[S,E]]
}