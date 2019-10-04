package net.reasoning.eqcel.compiler
import net.reasoning.eqcel.intermediate.ReifiedSheet
import net.reasoning.eqcel.intermediate.ExpandedSheet

object DevectorPhase extends Phase[ReifiedSheet, ExpandedSheet] {
  def transform(tree: ReifiedSheet): ExpandedSheet = ExpandedSheet(Seq())
}