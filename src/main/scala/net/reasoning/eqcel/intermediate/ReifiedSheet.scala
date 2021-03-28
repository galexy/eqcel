package net.reasoning.eqcel.intermediate

import net.reasoning.eqcel.expressions._
import net.reasoning.eqcel.formulas._

/** A reified sheet of formulas represented as deeply embedded expression trees.
  * 
  * This modules implements read-only expression trees that are reified versions
  * of the expression trees / formulas defined in Sheets. A Range in a Sheets
  * definition is converted into intermediate ReifiedExpressions.
  */
object ReifiedExpressions extends ExprModule {
  case class RangeIndexExpr(
    range: Int,
    index: Expr
  ) extends GenRangeIndexExpr[Int]
}

import ReifiedExpressions._

/** Intermediate form of Sheet program represented as a set of ReifiedRanges.
  *  
  * @param ranges
  */
case class ReifiedSheet(ranges: Seq[ReifiedRange])

/** Intermediate form of a formula range represented as a partial function.
  * 
  * A reified range represents a range within a spreadsheets as a partial
  * function from index within the range (cell) to the expression for that cell.
  *
  * @param id
  * @param start
  * @param end
  * @param definition
  * @param location
  */
case class ReifiedRange(
  id: Int, 
  start: Int, 
  end: Int, 
  definition: Int => Expr,
  location: Option[Location]
)