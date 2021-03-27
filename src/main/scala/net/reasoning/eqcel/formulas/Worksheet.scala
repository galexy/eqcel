package net.reasoning.eqcel.formulas

class Location {
}

trait RowPlacement[S <: Singleton with Int, E <: Singleton with Int] {
  def at(location: Location): RowPlacement[S, E] = ???
  def ++(right: RowPlacement[S, E]): RowPlacement[S, E] = ???
}

trait ColumnPlacement[S <: Singleton with Int, E <: Singleton with Int] {
  def at(location: Location): ColumnPlacement[S, E] = ???
  def ||(bottom: ColumnPlacement[S, E]): ColumnPlacement[S, E] = ???
}

object Placement {
  def row[S <: Singleton with Int, E <: Singleton with Int](range: Range[S, E]): RowPlacement[S, E] = ???
  def column[S <: Singleton with Int, E <: Singleton with Int](range: Range[S, E]): ColumnPlacement[S, E] = ???
  def apply[S <: Singleton with Int, E <: Singleton with Int](rows: RowPlacement[S, E]): RowPlacement[S, E] = ???
  def apply[S <: Singleton with Int, E <: Singleton with Int](columns: ColumnPlacement[S, E]): ColumnPlacement[S, E] = ???
}

trait Worksheet {
  val place = Placement  
  implicit def rangeToRow[S <: Singleton with Int, E <: Singleton with Int](range: Range[S, E]): RowPlacement[S, E] = ???
  implicit def rangeToColumn[S <: Singleton with Int, E <: Singleton with Int](range: Range[S, E]): ColumnPlacement[S, E] = ???
  
  def A(i: Int): Location = ???
}
