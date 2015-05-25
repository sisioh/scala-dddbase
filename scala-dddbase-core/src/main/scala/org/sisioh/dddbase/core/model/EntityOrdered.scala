package org.sisioh.dddbase.core.model

/**
 * 順序に対応したエンティティを実装するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait EntityOrdered[A, ID <: OrderedIdentifier[A, ID], E <: Entity[ID]]
    extends Ordered[E] {
  this: Entity[ID] =>

  def compare(that: E): Int = {
    identifier compare that.identifier
  }

}
