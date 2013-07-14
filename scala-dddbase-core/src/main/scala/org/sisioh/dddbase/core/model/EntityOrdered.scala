package org.sisioh.dddbase.core.model

/**
 * 順序に対応したエンティティを実装するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait EntityOrdered[A, ID <: OrderedIdentity[A, ID], T <: Entity[ID]]
  extends Ordered[T] {
  this: Entity[ID] =>

  def compare(that: T): Int = {
    identity compare that.identity
  }

}
