package net.reasoning.eqcel.compiler
import net.reasoning.eqcel.intermediate.LayoutSheet
import net.reasoning.eqcel.intermediate.ReifiedSheet
import net.reasoning.eqcel.intermediate.RowLayout
import net.reasoning.eqcel.intermediate.RangeLayouts

object LayoutPhase extends Phase[ReifiedSheet, LayoutSheet] {
  def transform(tree: ReifiedSheet): LayoutSheet = {
    // hard code to one row per range
    val layouts = tree.ranges.zipWithIndex.map {
      case (range, index) => (range.id, RowLayout((0, index), range.start, range.end))
    }

    val rangeLayouts = RangeLayouts(Map.from(layouts))

    LayoutSheet(rangeLayouts, tree.ranges)
  }
}