package net.reasoning.eqcel.formulas

import net.reasoning.eqcel.expressions._

/** Implementation module of ExprModule for Sheet DSL
  * 
  * The expressions implemented by this module are used by the Sheet trait
  * and represent expressions built by the user of the Sheet DSL. The 
  * expressions are an abstract syntax tree of the expressions users of
  * the DSL write in formula definitions.
  */
object SheetExpression extends ExprModule {
  case class RangeIndexExpr[S <: Singleton with Int, E <: Singleton with Int](
    range: Range[S,E],
    index: Expr
  )
  extends GenRangeIndexExpr[Range[S,E]]
}