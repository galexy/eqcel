package net.reasoning.eqcel
package test

import org.scalatest._
import net.reasoning.eqcel.formulas._
import net.reasoning.eqcel.compiler._
import net.reasoning.eqcel.intermediate._

class CompilerSuite extends FunSuite 
  with DiagrammedAssertions 
  with Matchers 
  with Inside {
    test("compile empty sheet") {
      object Model extends Sheet
      val compiler = new Compiler()
      val layout = compiler.compile(Model)
      assert(layout == LayoutSheet(Seq()))
    }
  }