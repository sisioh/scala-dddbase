package org.sisioh.dddbase.spec

/** ANDを表す仕様。
 *  @author j5ik2o
 */
class AndSpecification[T](private[spec] val spec1: Specification[T],
  private[spec] val spec2: Specification[T])
  extends Specification[T] {

  override def isSatisfiedBy(t: T) =
    spec1.isSatisfiedBy(t) && spec2.isSatisfiedBy(t);
}
