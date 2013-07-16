package org.sisioh.dddbase.core.model

/**
 * シリアライズに対応したエンティティを実装するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait EntitySerializable[ID <: Identity[_ <: java.io.Serializable], T <: Entity[ID]]
  extends Serializable {
  this: Entity[ID] =>

}
