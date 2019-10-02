package net.reasoning.eqcel

trait FinModel {

  // Dimensions of cells
  // -------------------
  // 1. How is a range of cells defined
  //    a. open for the user to specify input
  //    b. a constant value or sequence of constant values
  //    c. values read from an external source (file, database or service)
  //    d. a formula referencing potentially other ranges or functions
  //    e. a conditional expression ideally modeled as an if-else expression in 
  //       the host language. Open question - how do we capture this expression?
  //   Note: native spreadsheet functions will represented as stubs in the DSL
  //         and calls to user defined functions are captured
  // 2. The type of the value of the cell
  //    a. Number
  //    b. String
  //    c. Percentage
  //    d. Date, Time, Date and Time
  //    e. Currency
  // 3. The type of index into the range
  //    a. The index type must be enumerable 
  //       Ints, Years, Months, Weeks, Days
  //       or a fixed set constants like a list of countries
  // 4. Ranges or individual cells can be null?
  // 5. Cell computation can result in an error and errors should propogate
  // 6. Shape of the range - Cell, Linear Range, Matrix
  // 7. Ranges can be combined into tensors (disjoint ranges across multiple sheets)

  // Formulas should type check, meaning only legal combinations should be allowed
  // Ranges should be modeled abstractly that represent the dimensions specified above
  // A functional spreadsheet program is defined by combining ranges and cells
  // The result is a model of a functional spreadsheet program (the description of one)
  // This model is then reified by a concrete implementation.
  // One possible concrete implementation converts the program into a google spreadsheet
  // Another is a reactive program that can react to changes to input and/or constant cells
  // The spreadsheet implmentation will need to also "compile" user-defined functions 
  // into whatever udf mechanism is available - for example in google, they will have to
  // be compiled into javascript

  // trait Cell[V] {

  // }
  // trait OneDRange[Start <: Int, Size <: Int, V] {
  //   def apply(i: Int): Cell[V]
  // }
  
  // // Formula Definition Use cases and examples
  // implicit class Year(val year: Int) extends AnyVal {
  //   def to(toyear: Year): IndexedSeq[Year] = year.to(toyear)
  // }

  // val firstYear = 2000
  // val secondYear = firstYear + 1
  // val lastYear = 2018
  // type FirstYear = firstYear.type
  // type SecondYear = secondYear.type
  // type LastYear = lastYear.type

  // // 1. Defining a linear range of cells that are meant to be inputs of a certain type
  // //    and the type of the index and range of the index
  // lazy val revenue               = Money[FirstYear, LastYear](title = "Revenue")
  // lazy val otherOperatingRevenue = Money[FirstYear, LastYear](title = "Other Opearting Revenue")
  // lazy val expenses              = Money[FirstYear, LastYear](title = "Opearting Expenses")

  // // 2. Define a value (cell) of a certain type
  // lazy val growthRate = 50.percent
  // lazy val unitPrice = $(100.00)

  // // 3. Define an array that is a function of other arrays

  // // 3.1 - Define an array using a binary operator of two arrays
  // // Note - need to have type checker limit valid operations
  // // For example, Money + Money is fine, but Money * Money is not
  // // on the other hand Money * Percentage or Money * Number is fine
  // // and maybe Sting + Money?
  // // option 1
  // val totalRevenue          = revenue + otherOperatingRevenue
  // // option 2
  // val totalRevenue          = Money[FirstYear, LastYear](title="Total Revenue")
  // totalRevenue              := revenue + otherOperatingRevenue
  // // option 3
  // def totalRevenue          = revenue + otherOpeartingRevenue

  // // 3.2 - Define an array as the SUM/MIN/MAX (Monoid) of multiple arrays
  // val reportedEBITDA        = Sum(revenue, otherOperatingRevenue, expenses)

  // // 3.3 - Define an array as a conditional function of other arrays
  // val revenueOrZero         = if(revenue < 0) 0 else revenue

  // // 4 - Define an array from a column from a dataframe (or result of SQL query)
  // // Note: not sure how we define the bounds and or index of the array

  // // 5 - Define an array that is defined with a relative offset to one or more arrays
  // // e.g.  balance(thisyear) = balance(lastyear) + credits(thisyear) - debits(thisyear
  // // Note: not sure how to check that index into other arrays type checks
  // //       Can this be done with refined typess?
  // val endingBalance         = Money[FirstYear, LastYear](title="Ending Balance")
  // // option 1
  // val beginningBalance      = Money[SecondYear, LastYear](title="Beginning Balance") 
  // beginningBalance          := {\year => endingBalance(year-1)}

  // // option 2
  // def beginningBalance[SecondYear, LastYear](year: Year) = endingBalance(year-1)

  // // option 3
  // val beginningBalance = Money[SecondYear, LastYear](year => endingBalance(year-1))

  // // 6 - Combine ranges (including linear arrays) to form larger arrays

  // // 7 - Define arrays or ranges recursively
  // // e.g. CumulativeRevenue(2000) = Revenue(2000)
  // //      CumulativeRevenue(year) = Revenue(year) + CumulativeRevenue(year-1)
  // // option 2
  // val cumuRevenue = Money[FirstYear, LastYear]
  // cumuRevenue(FirstYear) := Revenue(FirstYear)
  // cumuRevenue            := \year => revenue(year) + cumuRevenue(year-1)

  // // 8 - Add special cases or overrides for an array
  // // e.g. ModeledRevenue(2000) = Revenue(2000)
  // //      ModeledRevenue(year) = ModeledRevenue(year-1) * 1.2
  // //      ModeledRevenue(2020) = ModeledRevenue(2019) * 1.5
  // // Note: not sure how to handle or require order of definitions being significant

  // // 9 - Define an array as a subset of an array

  // // Formatting use cases - TBD
}


trait FormulaDefinitions {

}

trait Formulas {
  implicit val formulaDefinitions: FormulaDefinitions = ???
}

trait Expression[T] {
  def +(right: Expression[T])(implicit fd: FormulaDefinitions): Expression[T] = ???
  def *(right: Expression[T])(implicit fd: FormulaDefinitions): Expression[T] = ???
  def :=(expr: Expression[T])(implicit fd: FormulaDefinitions): Unit = ???
  def :=(i: Int => Expression[T])(implicit fd: FormulaDefinitions): Unit = ???
}

trait Range[T] extends Expression[T] {
  def apply(index: Int)(implicit fd: FormulaDefinitions): Expression[T] = ???
  def apply[I <: Int]()(implicit fd: FormulaDefinitions): Expression[T] = ???
}

class Input[T] extends Expression[T]

object Input {
  def apply[T]()(implicit fd: FormulaDefinitions) = new Input[T]
}

class Money extends Range[Money] {
}

object Money {
  def apply()(implicit fd: FormulaDefinitions) = new Money()
}

import scala.language.implicitConversions
object DSL {
  implicit def intToMoneyExpr(i: Int): Expression[Money] = ???

  object same {
    def shape(money: Money): Money = new Money() // Not quite right
  }

  type Otherwise = 0
}

import DSL._
trait BaseFinModel extends Formulas {
  val growthRate = Money()

  type FirstYear = 2000

  val revenue          = Money()
  revenue[FirstYear]   := Input[Money]
  revenue[Otherwise]   := { (year) => revenue(year-1) * growthRate } // Hmm... hardcoding

  val opeartingRevenue = same shape revenue
  opeartingRevenue     := Input[Money]

  val totalRevenue     = same shape revenue
  totalRevenue         := revenue + opeartingRevenue

  val cumuRevenue        = same shape revenue
  cumuRevenue[FirstYear] := revenue[FirstYear]
  cumuRevenue            := { (year) => cumuRevenue(year - 1) + revenue(year) }
}

trait SpecializedFinModel extends BaseFinModel {
  revenue[FirstYear] := 10_000_000
}