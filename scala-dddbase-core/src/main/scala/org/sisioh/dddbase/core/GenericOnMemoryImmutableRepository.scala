package org.sisioh.dddbase.core

/**
 * 汎用的な非同期型オンメモリ不変リポジトリ。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
class GenericOnMemoryImmutableRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryImmutableRepository[GenericOnMemoryImmutableRepository[ID, T], ID, T]

