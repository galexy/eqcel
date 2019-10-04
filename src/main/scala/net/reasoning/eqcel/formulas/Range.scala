package net.reasoning.eqcel.formulas

import SheetExpression._

/** Trait that defines abstractly ranges within a spreadsheet program.
  * Ranges can represent a cell, vector, matrix or tensor.
  * Ranges have several different dimension
  * 1. Shape - cell, vector, matrix, tensor
  * 2. Type  - Number, Currency, Date, String, AnyVal
  * 3. Index Type - by Int, Date (Year, Month, Week, Date), Enumeration
  * 4. Bounds - Starting and ending index (1 to 10) or (Jan to Dec)
  * 5. Definition - Empty, Input from user, formula
  * 
  * Ranges can be defined by a formula and can have specific cells
  * override
  * {{{
  * val nextYear: FormulaRange = (year: Expr) => year + 1
  * nextYear(1999) = 2001
  * }}}
  */ 
sealed trait Range[S <: Singleton with Int, E <: Singleton with Int] {
  import scala.collection.mutable.Map
  private[eqcel] val overrides: Map[Int, Expr] = Map()

  def apply(index: Expr): Expr = RangeIndexExpr(this, index)

  def update
    (index: Int, formula: Expr)
    (implicit s: ValueOf[S], e: ValueOf[E], sheet: Sheet): Unit = {
      require(index >= s.value && index < e.value, s"Index $index out of range")
      require(!overrides.contains(index), "Index $index has already been overriden for this range")

      sheet.registerOverride(this, index -> formula)
      overrides += index -> formula
    }

  private[eqcel] def formula(indexExpr: IntLit)(implicit s: ValueOf[S], e: ValueOf[E]): Expr = {
    val index = indexExpr.value
    require(index >= s.value && index < e.value, s"Index $index out of range")
    overrides.getOrElse(index, baseFormula(indexExpr))
  }

  protected def baseFormula(index: IntLit): Expr
}

object Range {
  def apply[S <: Singleton with Int, E <: Singleton with Int]
    ()(implicit sheet: Sheet, s: ValueOf[S], e: ValueOf[E]): Range[S,E] = 
      EmptyLinearRange[S,E]()

  def apply[S <: Singleton with Int, E <: Singleton with Int]
    (formula: Expr => Expr)
    (implicit sheet: Sheet, s: ValueOf[S], e: ValueOf[E]): Range[S,E] = 
      new FormulaRange[S,E](formula)
}

class EmptyLinearRange[S <: Singleton with Int, E <: Singleton with Int] private[formulas] 
  () (implicit sheet: Sheet, s: ValueOf[S], e: ValueOf[E])
  extends Range[S,E] {
    sheet.register(this, _ => Empty)

    def baseFormula(index: IntLit) = Empty

    override def toString(): String = s"EmptyLinearRange($hashCode)"
  }

object EmptyLinearRange {
  def apply[S <: Singleton with Int, E <: Singleton with Int]
    ()(implicit sheet: Sheet, s: ValueOf[S], e: ValueOf[E]) =
      new EmptyLinearRange[S,E]()

  def unapply[S <: Singleton with Int, E <: Singleton with Int](e: EmptyLinearRange[S,E]) =
    Some()
}

class FormulaRange[S <: Singleton with Int, E <: Singleton with Int] private[formulas] 
  (val definition: IntLit => Expr)
  (implicit sheet: Sheet, s: ValueOf[S], e: ValueOf[E])
  extends Range[S,E] {
    sheet.register(this, definition)

    def baseFormula(index: IntLit) = definition(index)

    override def toString(): String = s"FormulaRange($hashCode)"
  }

object FormulaRange {
  def apply[S <: Singleton with Int, E <: Singleton with Int]
    (definition: Expr => Expr)
    (implicit sheet: Sheet, s: ValueOf[S], e: ValueOf[E]) =
      new FormulaRange[S,E](definition)

  def unapply[S <: Singleton with Int, E <: Singleton with Int](f: FormulaRange[S,E]) =
    Some(f.definition)
}