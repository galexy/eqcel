package net.reasoning.eqcel.compiler

trait Phase[T,U] {
  def transform(tree: T): U
}

class Compiler {
  val compile = (ReifyFormulaPhase.transform _) andThen
                (FlattenPhase.transform _) andThen
                (LayoutPhase.transform _)
}