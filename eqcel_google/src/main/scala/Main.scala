import net.reasoning.eqcel.formulas._
import net.reasoning.eqcel.formulas.SheetExpression.Expr
import net.reasoning.eqcel.compiler.Compiler
import javax.lang.model.util.Elements.Origin

trait Model extends Formulas {
  val fixed: Range[0, 10] = FormulaRange { _ => 100 }

  lazy val revenue: Range[0,10] = { year : Expr => 100 + revenue(year-1) }
  revenue(0) = 0

  val cumuRevenue: Range[0,10] = { year : Expr => 
    cumuRevenue(year-1) + revenue(year) 
  }
  cumuRevenue(0) = revenue(0)
}

object Original extends Model {

}

object Scenario1 extends Model {
  override lazy val revenue = FormulaRange { year: Expr => 100 }
}

case class Sheet(model: Model) extends Worksheet {
  place row model.revenue at A(1)
  place row model.cumuRevenue at A(3)
  place row model.fixed at A(5)
}

object QuickStart extends App {
  val googleSheetBuilder = new GoogleSheetCompiler()
  val compile = ((new Compiler()).compile _)()

  val original = compile(Sheet(Original))
  googleSheetBuilder.build(original, "Original Model")

  val scenario1 = compile(Sheet(Scenario1))
  googleSheetBuilder.build(scenario1, "Scenario 1")
}
