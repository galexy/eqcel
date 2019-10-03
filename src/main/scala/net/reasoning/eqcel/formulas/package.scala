package net.reasoning.eqcel

/** This package supplies the developer facing DSL for constructing
  * spreadsheet programs. Programs consist of vectors and matrixes
  * that can be defined by equations.
  */
package object formulas {
  implicit def intToExpr(i: Int): Expr = IntLit(i)

  implicit def funcToFormula[S <: Int, E <: Int](e: Expr => Expr): FormulaRange[S,E] = 
    FormulaRange(e)
}
