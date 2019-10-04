package net.reasoning.eqcel.compiler
import net.reasoning.eqcel.formulas.Sheet
import net.reasoning.eqcel.intermediate.ExpandedSheet

object DevectorPhase extends Phase[Sheet, ExpandedSheet] {
  def transform(tree: Sheet): ExpandedSheet = ExpandedSheet(Seq())
}