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

  trait Sheet {
    type FirstYear = 2000
    type LastYear = 2018
    type OnePast = 2019

    type Money = Long

    def revenue               = Money[FirstYear, LastYear](title = "Revenue")
    def otherOperatingRevenue = Money[FirstYear, LastYear](title = "Other Operating Revenue")
    def costOfGoodsSold       = Money[FirstYear, LastYear](title = "Other Operating Revenue")
    def sga                   = Money[FirstYear, LastYear](title = "Selling, Gen & Admin Expense")
    def depExp                = Money[FirstYear, LastYear](title = "Selling, Gen & Admin Expense")
    def otherOperatingExpense = Money[FirstYear, LastYear](title = "Other Operating Expense")

    def totalRevenue          = revenue + otherOperatingRevenue
  }

}