package net.reasoning.eqcel.compiler
import net.reasoning.eqcel.intermediate.ExpandedSheet
import net.reasoning.eqcel.intermediate.LayoutSheet

object LayoutPhase extends Phase[ExpandedSheet, LayoutSheet] {
  def transform(tree: ExpandedSheet): LayoutSheet = LayoutSheet(Seq())
}