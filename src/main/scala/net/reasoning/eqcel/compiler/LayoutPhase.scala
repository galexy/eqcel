package net.reasoning.eqcel.compiler
import net.reasoning.eqcel.intermediate.LayoutSheet
import net.reasoning.eqcel.intermediate.ReifiedSheet
import net.reasoning.eqcel.intermediate.RowLayout
import net.reasoning.eqcel.intermediate.RangeLayouts

/** Compiler phase that layouts out ranges onto a spreadsheet.
  * 
  */
object LayoutPhase extends Phase[ReifiedSheet, LayoutSheet] {
  def transform(tree: ReifiedSheet): LayoutSheet = {
    // TODO: Check that layouts do not overlap
    // TODO: Check that ranges are not layed out more than once
    // TODO: Fix syntax error of no A1 location for a placement

    val layouts = tree.ranges.map {
       r => (r.id, RowLayout(r.location.get, r.start, r.end))
    }
  
    val rangeLayouts = RangeLayouts(Map.from(layouts))

    LayoutSheet(rangeLayouts, tree.ranges)
  }
}
