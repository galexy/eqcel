package net.reasoning.eqcel.intermediate

case class ExpandedSheet(ranges: Seq[ExpandedRange])
case class ExpandedRange(name: String, cells: Seq[Cell])

sealed trait Cell
case class IntCell(value: Int) extends Cell
case class FormulaCell(formula: String) extends Cell
object EmptyCell extends Cell {
  override def toString(): String = "EmptyCell"
}