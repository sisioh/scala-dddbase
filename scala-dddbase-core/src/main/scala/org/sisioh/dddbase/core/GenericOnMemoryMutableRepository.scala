package org.sisioh.dddbase.core

/**
 * 汎用的な同期型オンメモリ可変リポジトリ。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
class GenericOnMemoryMutableRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryMutableRepository[GenericOnMemoryMutableRepository[ID, T], ID, T]

