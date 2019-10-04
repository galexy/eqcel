package net.reasoning.eqcel.expressions

/** Expression module for representing expressions trees from 
  * the sheets DSL and later phases of the compiler.
  * 
  */
trait ExprModule {
  sealed trait Expr {
    def +(e: Expr): Expr = Add(this, e)
    def -(e: Expr): Expr = Sub(this, e)
  }

  object Empty extends Expr
  case class Add(left: Expr, right: Expr) extends Expr
  case class Sub(left: Expr, right: Expr) extends Expr
  case class Mul(left: Expr, right: Expr) extends Expr
  case class Div(left: Expr, right: Expr) extends Expr

  case class Var(name: String) extends Expr
  case class IntLit(value: Int) extends Expr

  abstract class GenRangeIndexExpr[T] extends Expr {
    val range: T
    val index: Expr
  }
}
