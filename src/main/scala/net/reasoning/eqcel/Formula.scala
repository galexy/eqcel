package net.reasoning.eqcel

object FinModel extends App {

  implicit class Year(val year: Int) extends AnyVal {
    def to(toyear: Year): IndexedSeq[Year] = year.to(toyear)
  }

  //Trait that represents a logical one dimensional array in a spreadsheet
  trait LinearCellRange[S <: Int, E <: Int, V] {
    def range(implicit s: ValueOf[S], e: ValueOf[E]): Range = s.value to e.value
    def apply(index: Int): V = ???

    def + (right: LinearCellRange[S,E,V]): LinearCellRange[S,E,V] = ???
    def - (right: LinearCellRange[S,E,V]): LinearCellRange[S,E,V] = ???
  }

  trait TitledRange {
    def title: String
  }

  // An Input is an array that can filled in with user input
  abstract class Input[S <: Int, E <: Int, V] extends LinearCellRange[S,E,V] with TitledRange {

  }

  type MoneyRep = Long

  class Money[S <: Int, E <: Int](val title: String) extends Input[S,E,MoneyRep] {
    
  }

  // Factory
  object Money {
    def apply[S <: Int, E <: Int](title: String) = new Money[S,E](title)
  }

  val firstYear = 2000
  val secondYear = firstYear + 1
  val lastYear = 2018
  type FirstYear = firstYear.type
  type SecondYear = secondYear.type
  type LastYear = lastYear.type

  // Formula Definition Use cases

  // 1. Defining a linear range of cells that are meant to be inputs of a certain type
  //    and the type of the index and range of the index
  val revenue               = Money[FirstYear, LastYear](title = "Revenue")
  val otherOperatingRevenue = Money[FirstYear, LastYear](title = "Other Operating Revenue")
  val costOfGoodsSold       = Money[FirstYear, LastYear](title = "Other Operating Revenue")
  val sga                   = Money[FirstYear, LastYear](title = "Selling, Gen & Admin Expense")
  val depExp                = Money[FirstYear, LastYear](title = "Selling, Gen & Admin Expense")
  val otherOperatingExpense = Money[FirstYear, LastYear](title = "Other Operating Expense")

  // 2. Define a value (cell) of a certain type

  // 3. Define an array that is a function of other arrays

  // 3.1 - Define an array using a binary operator of two arrays
  val totalRevenue          = revenue + otherOperatingRevenue

  println(revenue.range)

  // 3.2 - Define an array as the SUM/MIN/MAX (Monoid) of multiple arrays

  // 3.3 - Define an array as a conditional function of other arrays

  // 4 - Define an array from a column from a dataframe (or result of SQL query)
  // Note: not sure how we define the bounds and or index of the array

  // 5 - Define an array that is defined with a relative offset to one or more arrays
  // e.g.  balance(thisyear) = balance(lastyear) + credits(thisyear) - debits(thisyear
  // Note: not sure how to check that index into other arrays type checks
  //       Can this be done with refined typess?

  // 6 - Combine ranges (including linear arrays) to form larger arrays

  // 7 - Define arrays or ranges recursively
  // e.g. CumulativeRevenue(2000) = Revenue(2000)
  //      CumulativeRevenue(year) = Revenue(year) + CumulativeRevenue(year-1)

  // 8 - Add special cases or overrides for an array
  // e.g. ModeledRevenue(2000) = Revenue(2000)
  //      ModeledRevenue(year) = ModeledRevenue(year-1) * 1.2
  //      ModeledRevenue(2020) = ModeledRevenue(2019) * 1.5
  // Note: not sure how to handle or require order of definitions being significant

  // 9 - Define an array as a subset of an array

  // Formatting use cases - TBD
 }