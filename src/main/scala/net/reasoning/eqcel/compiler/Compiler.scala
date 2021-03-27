package net.reasoning.eqcel.compiler


/** Trait that defines a compiler phase.
  * 
  * A compiler phase provides a transform function that converts "trees/dags"
  * from one phase to the next. Each phase represents the program with
  * a tree structure specific that phase.
  */
trait Phase[T,U] {
  def transform(tree: T): U
}

/** Eqcel compiler.
 * 
 *  the compiler tranforms a program that implements the
 * [[net.reasoning.eqcel.formulas.Formulas]] trait into a 
 * [[net.reasoning.eqcel.intermediate.ExpandedSheet]].
 */
class Compiler {
  val compile = (ReifyFormulaPhase.transform _) andThen
                (LayoutPhase.transform _) andThen
                (FlattenPhase.transform _)
}