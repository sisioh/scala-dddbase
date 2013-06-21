package org.sisioh.dddbase.core

/**
 * 汎用的な非同期型オンメモリ可変リポジトリ。
 *
 * @param core 内部で利用するオンメモリ可変リポジトリ。
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
class GenericAsyncOnMemoryMutableRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
(protected val core: GenericOnMemoryMutableRepository[ID, T] = new GenericOnMemoryMutableRepository[ID, T])
  extends AsyncOnMemoryRepository[GenericAsyncOnMemoryMutableRepository[ID, T], GenericOnMemoryMutableRepository[ID, T], ID, T] {

  protected def createInstance(state: GenericOnMemoryMutableRepository[ID, T]): GenericAsyncOnMemoryMutableRepository[ID, T] =
    new GenericAsyncOnMemoryMutableRepository[ID, T](state)

}
