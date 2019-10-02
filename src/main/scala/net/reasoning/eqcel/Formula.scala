package net.reasoning.eqcel

object FinModel extends App {

  implicit class Year(val year: Int) extends AnyVal {
    def to(toyear: Year): IndexedSeq[Year] = year.to(toyear)
  }

  trait LinearCellRange[S <: Int, E <: Int, V] extends PartialFunction[Int,V] {
    def range(implicit s: ValueOf[S], e: ValueOf[E]): Range = s.value to e.value
    def apply(index: Int): V = ???

    def isDefinedAt(x: Int): Boolean = ??? // can't seem to literal type values

    def + (right: LinearCellRange[S,E,V]): LinearCellRange[S,E,V] = ???
    def - (right: LinearCellRange[S,E,V]): LinearCellRange[S,E,V] = ???

  }

  class Input[S <: Int, E <: Int, V](title: String) extends LinearCellRange[S,E,V] {

  }

  trait Sheet {
    type FirstYear = 2000
    type LastYear = 2018
    type OnePast = 2019

    type Money = Long

    def revenue               = new Input[FirstYear, LastYear, Money](title = "Revenue")
    def otherOperatingRevenue = new Input[FirstYear, LastYear, Money](title = "Other Operating Revenue")
    def costOfGoodsSold       = new Input[FirstYear, LastYear, Money](title = "Other Operating Revenue")
    def sga                   = new Input[FirstYear, LastYear, Money](title = "Selling, Gen & Admin Expense")
    def depExp                = new Input[FirstYear, LastYear, Money](title = "Selling, Gen & Admin Expense")
    def otherOperatingExpense = new Input[FirstYear, LastYear, Money](title = "Other Operating Expense")

    def totalRevenue          = revenue + otherOperatingRevenue
    
  }

}