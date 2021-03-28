package net.reasoning.eqcel.formulas

case class Location private [formulas] (column: Int, row: Int) 

private [eqcel] trait Placement {
  private [eqcel] def rangeId: Int
  private [eqcel] def formulas: Formulas
  def location: Option[Location]
}

class RowPlacement[S <: Singleton with Int, E <: Singleton with Int]
  (range: Range[S, E])
  (implicit sheet: Worksheet, s: ValueOf[S], e: ValueOf[E])
  extends Placement {
    sheet.register(this)

    private [eqcel] def rangeId = range.hashCode()
    private [eqcel] def formulas = range.formulasContainer

    var location: Option[Location] = None

    def at(location: Location): RowPlacement[S, E] = {
      this.location = Some(location)
      this
    }

    def ++(right: RowPlacement[S, E]): RowPlacement[S, E] = ???
  }

private [eqcel] object RowPlacement {
  def apply[S <: Singleton with Int, E <: Singleton with Int]
    (range: Range[S, E])(implicit worksheet: Worksheet, s: ValueOf[S], e: ValueOf[E]) = 
      new RowPlacement[S, E](range)
}

private [eqcel] class ColumnPlacement[S <: Singleton with Int, E <: Singleton with Int]
  (range: Range[S, E])
  (implicit sheet: Worksheet, s: ValueOf[S], e: ValueOf[E])
  extends Placement {

    private [eqcel] def rangeId = range.hashCode()
    private [eqcel] def formulas = range.formulasContainer

    var location: Option[Location] = None
    
    def at(location: Location): ColumnPlacement[S, E] = ???
    def ||(bottom: ColumnPlacement[S, E]): ColumnPlacement[S, E] = ???
  }

private [eqcel] object ColumnPlacement {
  def apply[S <: Singleton with Int, E <: Singleton with Int]
    (range: Range[S, E])(implicit worksheet: Worksheet, s: ValueOf[S], e: ValueOf[E]) = 
      new ColumnPlacement[S, E](range)
}

private [formulas] class Place (implicit worksheet: Worksheet) {
  def row[S <: Singleton with Int, E <: Singleton with Int](range: Range[S, E])
    (implicit s: ValueOf[S], e: ValueOf[E]) = RowPlacement(range)

  def column[S <: Singleton with Int, E <: Singleton with Int](range: Range[S, E]) 
    (implicit s: ValueOf[S], e: ValueOf[E])= ColumnPlacement(range)

  def apply[S <: Singleton with Int, E <: Singleton with Int](row: RowPlacement[S, E]) = row

  def apply[S <: Singleton with Int, E <: Singleton with Int](column: ColumnPlacement[S, E]) = column
}

trait Worksheet { self =>
  implicit val sheet = self
  val place = new Place()

  // Implicit type converts from Range to Rows or Columns
  implicit def rangeToRow[S <: Singleton with Int, E <: Singleton with Int](range: Range[S, E]): RowPlacement[S, E] = ???
  implicit def rangeToColumn[S <: Singleton with Int, E <: Singleton with Int](range: Range[S, E]): ColumnPlacement[S, E] = ???
  
  import scala.collection.mutable.{Set => MSet}
  val placements = MSet[Placement]()

  def placedRanges = placements.toSeq

  // register placement ASTs
  def register(placement: Placement) = {
    placements.add(placement)
  }

  // Methods to provide A1 like locations
  def A(i: Int): Location = Location(1, i)
  def B(i: Int): Location = Location(2, i)
}
