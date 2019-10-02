package net.reasoning.eqcel

object FinModel extends App {

  implicit class Year(val year: Int) extends AnyVal {
    def to(toyear: Year): IndexedSeq[Year] = year.to(toyear)
  }

  trait LinearCellRange[I, V] extends PartialFunction[I, V] {
    def range: IndexedSeq[I]
    def apply(index: I): V = ???
    def isDefinedAt(index: I): Boolean = range.contains(index)
  }

  class Input[I, V](val range: IndexedSeq[I], title: String) extends LinearCellRange[I, V] {

  }

  trait Sheet {
    val firstYear = Year(1994)
    val lastYear = Year(2018)

    type Money = Long

    def revenue               = new Input[Year, Money](firstYear to lastYear, title = "Revenue")
    def otherOperatingRevenue = new Input[Year, Money](firstYear to lastYear, title = "Other Operating Revenue")
    def costOfGoodsSold       = new Input[Year, Money](firstYear to lastYear, title = "Other Operating Revenue")
    def sga                   = new Input[Year, Money](firstYear to lastYear, title = "Selling, Gen & Admin Expense")
    def depExp                = new Input[Year, Money](firstYear to lastYear, title = "Selling, Gen & Admin Expense")
    def otherOperatingExpense = new Input[Year, Money](firstYear to lastYear, title = "Other Operating Expense")


  }

}