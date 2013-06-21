package org.sisioh.dddbase.core

/**
 * オンメモリリポジトリを表すトレイト。
 *
 * @tparam R 当該リポジトリを実装する派生型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
trait OnMemoryRepository[R <: OnMemoryRepository[R, ID, T], ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
  extends Repository[R, ID, T] with EntityIterableReader[ID, T] with EntityReaderByOption[ID, T] with Cloneable {

}
