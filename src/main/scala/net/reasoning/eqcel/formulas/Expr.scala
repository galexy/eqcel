package net.reasoning.eqcel.formulas

/** Expression tree extracted from deep EDSL
  *
  */
sealed trait Expr {
  def +(e: Expr): Expr = Add(this, e)
  def -(e: Expr): Expr = Sub(this, e)
}

object Empty extends Expr
case class Add(left: Expr, right: Expr) extends Expr
case class Sub(left: Expr, right: Expr) extends Expr
case class Mul(left: Expr, right: Expr) extends Expr
case class Div(left: Expr, right: Expr) extends Expr

case class RangeIndexExpr[S <: Singleton with Int, E <: Singleton with Int](
  range: Range[S,E], 
  index: Expr
) extends Expr {

}

case class Var(name: String) extends Expr
case class IntLit(value: Int) extends Expr