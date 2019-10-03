package net.reasoning.eqcel.formulas

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
sealed trait Range {
  var rangeName: Option[String] = None

  def apply(index: Expr): Expr = RangeIndexExpr(this, index)
  def update(index: Int, expr: Expr): Unit = {
    // for the moment, no need to update the sheet env
  }

  def formula(index: Expr): Expr

  def name: Option[String] = rangeName
  def name_=(s:String):Unit = { rangeName = Some(s) }
}

object Range {
  def apply(): Range = LinearRange()
  def apply(formula: Expr => Expr): Range = new FormulaRange(formula)
}

case class LinearRange() extends Range {
  def formula(index: Expr) = Empty
}

case class FormulaRange(definition: Expr => Expr) extends Range {
  def formula(index: Expr) = definition(index)
}