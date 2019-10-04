package net.reasoning.eqcel.intermediate

case class RangeLayouts(layouts: Map[Int, Layout])

case class LayoutSheet(
  rangeLocations: RangeLayouts, 
  rangesDefintions: Seq[ReifiedRange]
)

trait Layout {
  def reference(index: Int): String
  def fullAddress: String
}

case class RowLayout(firstCell: (Int, Int), startIndex: Int, endIndex: Int) 
  extends Layout {
    def reference(index: Int): String = A1Name(firstCell._1 + index, firstCell._2)
    def fullAddress: String = 
      s"${A1Name(firstCell._1, firstCell._2)}:${A1Name(firstCell._1 + endIndex, firstCell._2)}"

    def A1Name(column: Int, row: Int): String = 
      s"${('A'.toInt + column).toChar}${(row + 1).toString}"
  }