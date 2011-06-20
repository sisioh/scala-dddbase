package org.sisioh.dddbase.spec

/** 否定の仕様を表すモデル。
 *
 *  <p>ある `Specification` の否定をとる `Specification` 実装クラス。
 *  デコレータではないので注意。</p>
 *
 *  @tparam T [org.sisioh.dddbase.spec.NotSpecification]]の型
 */
class NotSpecification[T](private[spec] val spec1: Specification[T])
  extends Specification[T] {

  override def isSatisfiedBy(t: T) =
    spec1.isSatisfiedBy(t) == false

}
