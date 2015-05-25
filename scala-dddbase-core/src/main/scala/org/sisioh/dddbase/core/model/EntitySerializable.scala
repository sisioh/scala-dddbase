package org.sisioh.dddbase.core.model

/**
 * シリアライズに対応したエンティティを実装するためのトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait EntitySerializable[ID <: Identifier[_ <: java.io.Serializable], E <: Entity[ID]]
    extends Serializable {
  this: Entity[ID] =>

}
