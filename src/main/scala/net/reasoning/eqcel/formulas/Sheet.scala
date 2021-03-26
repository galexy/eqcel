package net.reasoning.eqcel.formulas

import scala.collection.mutable

import SheetExpression._

/** Trait for building spreadsheets out of formulas.
  * 
  * Implementations of this trait represent spreadsheet programs as a series
  * of ranges defined by formulas. This is the top level embedded DSL for
  * eqcel. Users of the DSL create objects that implement the Sheet trait.
  * Ranges are vals with in the sheet object and are defined as formulas,
  * which are functions of Expr => Expr. The embedded DSL is the formula
  * definitions and as formulas and ranges are defined, they register themsevles
  * with the Sheet. In this sense, the Sheet is a mutable expression tree
  * that users create by defining vals within the Sheet object.
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