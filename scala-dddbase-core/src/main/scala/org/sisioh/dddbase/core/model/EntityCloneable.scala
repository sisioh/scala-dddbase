package org.sisioh.dddbase.core.model

/**
 * クローンに対応したエンティティを実装するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait EntityCloneable[ID <: Identity[_], T <: Entity[ID]]
  extends Cloneable {
  this: Entity[ID] =>

  /**
   * クローンを生成する。
   *
   * @return クローンしたインスタンス
   */
  override def clone: T =
    super.clone.asInstanceOf[T]

}
