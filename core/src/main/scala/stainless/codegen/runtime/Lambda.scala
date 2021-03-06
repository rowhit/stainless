/* Copyright 2009-2018 EPFL, Lausanne */

package stainless.codegen.runtime

abstract class Lambda {
  def apply(args: Array[AnyRef]): AnyRef
}
