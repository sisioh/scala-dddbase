package org.sisioh.dddbase.core.model

/**
 * クローンに対応したエンティティを実装するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait EntityCloneable[ID <: Identifier[_], E <: Entity[ID]]
    extends Cloneable {
  this: Entity[ID] =>

  /**
   * クローンを生成する。
   *
   * @return クローンしたインスタンス
   */
  override def clone: E =
    super.clone.asInstanceOf[E]

}
