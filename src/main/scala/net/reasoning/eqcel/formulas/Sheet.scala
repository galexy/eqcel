package net.reasoning.eqcel.formulas

import scala.collection.mutable

import SheetExpression._

/** Trait used to declare formulas for a logic sheet
  * 
  */
trait Sheet { self =>
  implicit val sheet = self
  implicit def intToExpr(i: Int): IntLit = IntLit(i)
  implicit def funcToFormula[S <: Singleton with Int, E <: Singleton with Int]
    (formula: Expr => Expr)(implicit sheet: Sheet, s: ValueOf[S], e: ValueOf[E]): FormulaRange[S,E] = 
    FormulaRange(formula)
  
  type HashCode = Int
  type IndexedFormula = IntLit => Expr

  import scala.collection.mutable.{Map => MMap}
  val symbolTable = MMap[HashCode, RangeMetadata]()

  def ranges:Seq[RangeMetadata] = symbolTable.values.toIndexedSeq

  def register[S <: Singleton with Int, E <: Singleton with Int]
    (r: Range[S,E], formula: IndexedFormula)(implicit s: ValueOf[S], e: ValueOf[E]) = {
      val hashId = r.hashCode
      symbolTable += hashId -> RangeMetadata(hashId, s.value, e.value, formula)
    }

  def registerOverride[S <: Singleton with Int, E <: Singleton with Int]
    (r: Range[S,E], newOverride: (Int, Expr))(implicit s: ValueOf[S], e: ValueOf[E]) = {
      val hashCode = r.hashCode
      val rangeMetadata = symbolTable(hashCode)

      val newMetadata = rangeMetadata.copy(overrides = rangeMetadata.overrides + newOverride)
      symbolTable put (hashCode, newMetadata)
    }
}

private[eqcel] case class RangeMetadata(
  hashId: Int, 
  start: Int, 
  end: Int,
  baseFormula: IntLit => Expr,
  overrides: Map[Int, Expr] = Map()
)