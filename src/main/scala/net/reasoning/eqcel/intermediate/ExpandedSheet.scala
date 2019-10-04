package net.reasoning.eqcel.intermediate

case class ExpandedSheet(ranges: Seq[ExpandedRange])
case class ExpandedRange(name: String, cells: Seq[Cell])

sealed trait Cell
case class IntCell(value: Int) extends Cell
case class FormulaCell(formula: UnrefFormulaExpr) extends Cell
object EmptyCell extends Cell

sealed trait UnrefFormulaExpr

case class OpUFExpr(
  op: Op, 
  left: UnrefFormulaExpr, 
  right: UnrefFormulaExpr
) extends UnrefFormulaExpr

case class IndexRangeRefUFExpr(
  rangeRef: String,
  index: IntUFExpr
) extends UnrefFormulaExpr

case class IntUFExpr(value: Int) extends UnrefFormulaExpr

sealed trait Op
object Plus extends Op
object Minus extends Op

