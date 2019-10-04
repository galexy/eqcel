package net.reasoning.eqcel.compiler

import net.reasoning.eqcel.formulas.Sheet
import net.reasoning.eqcel.intermediate.ReifiedSheet

object ReifyFormulaPhase extends Phase[Sheet, ReifiedSheet] {
  def transform(tree: Sheet): ReifiedSheet = ReifiedSheet(Seq())
}