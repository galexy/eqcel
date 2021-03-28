package net.reasoning.eqcel.intermediate

import net.reasoning.eqcel.formulas.Location

case class RangeLayouts(layouts: Map[Int, Layout])

case class LayoutSheet(
  rangeLocations: RangeLayouts, 
  rangesDefintions: Seq[ReifiedRange]
)

trait Layout {
  def reference(index: Int): String
  def fullAddress: String
}

case class RowLayout(firstCell: Location, startIndex: Int, endIndex: Int) 
  extends Layout {
    def reference(index: Int): String = A1Name(firstCell.column + index, firstCell.row)
    def fullAddress: String = 
      s"${A1Name(firstCell.column, firstCell.row)}:${A1Name(firstCell.column + endIndex, firstCell.row)}"

    def A1Name(column: Int, row: Int): String = 
      s"${('A'.toInt - 1 + column).toChar}${(row).toString}"
  }