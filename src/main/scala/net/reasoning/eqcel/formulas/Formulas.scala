package net.reasoning.eqcel.formulas

import scala.collection.mutable
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

import SheetExpression._

/** Trait for defining ranges and formulas for a spreadsheet.
  * 
  * Implementations of this trait represent spreadsheet programs as a series
  * of ranges defined by formulas. This is the top level embedded DSL for
  * eqcel. Users of the DSL create objects that implement the Formulas trait.
  * Ranges are vals with in the sheet object and are defined as formulas,
  * which are functions of Expr => Expr. The embedded DSL is the formula
  * definitions and as formulas and ranges are defined, they register themsevles
  * with the Formulas. In this sense, the Sheet is a mutable expression tree
  * that users create by defining vals within the Formulas object.
  */
trait Formulas { self =>
  // Implicit to the current sheet for methods that need a reference
  implicit val formulas = self

  // Implicitly converter literl integers into Expression
  implicit def intToExpr(i: Int): IntLit = IntLit(i)

  // Implicitly convert functions of Expr => Expr to a FormulaRange
  implicit def funcToFormula[S <: Singleton with Int, E <: Singleton with Int]
    (formula: Expr => Expr)(implicit formulas: Formulas, s: ValueOf[S], e: ValueOf[E]): FormulaRange[S,E] = 
    FormulaRange(formula)
  
  type HashCode = Int
  type IndexedFormula = IntLit => Expr

  // Mutable Map for registering the ranges defined in an instance of Formulas
  import scala.collection.mutable.{Map => MMap}
  val symbolTable = MMap[HashCode, RangeMetadata]()

  def ranges:Seq[RangeMetadata] = symbolTable.values.toIndexedSeq

  def range(hashId: HashCode) = symbolTable(hashId)

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