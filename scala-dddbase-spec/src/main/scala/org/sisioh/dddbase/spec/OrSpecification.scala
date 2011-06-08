package org.sisioh.dddbase.spec

/** 論理和の仕様を表すモデル。
 *
 *  <p>2つの [[Specification]] の論理和をとる [[Specification]] 実装クラス。</p>
 *
 *  @tparam T [[OrSpecification]]の型
 */
class OrSpecification[T](private[spec] val spec1: Specification[T],
  private[spec] val spec2: Specification[T])
  extends Specification[T] {

  override def isSatisfiedBy(t: T) =
    spec1.isSatisfiedBy(t) || spec2.isSatisfiedBy(t);

}