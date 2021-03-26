package net.reasoning.eqcel.expressions

/** Expression module for representing expressions trees from 
  * the sheets DSL and later phases of the compiler.
  * 
  * This module extracts out common parts of expression trees that would 
  * otherwise be repeated in each compiler phase that had a need for 
  * representing expressions.
  * 
  * Modules that implement this trait can introduce expressions that
  * are specific to that phase. Implementations of this trait need to 
  * provide concrete implementions of abstract Expr types.
  * 
  */
trait ExprModule {
  trait Expr {
    def +(e: Expr): Expr = Add(this, e)
    def -(e: Expr): Expr = Sub(this, e)
  }

  object Empty extends Expr
  case class Add(left: Expr, right: Expr) extends Expr
  case class Sub(left: Expr, right: Expr) extends Expr
  // case class Mul(left: Expr, right: Expr) extends Expr
  // case class Div(left: Expr, right: Expr) extends Expr

  case class IntLit(value: Int) extends Expr

  abstract class GenRangeIndexExpr[T] extends Expr {
    val range: T
    val index: Expr
  }
}
