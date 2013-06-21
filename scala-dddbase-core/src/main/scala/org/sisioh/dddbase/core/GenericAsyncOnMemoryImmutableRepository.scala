package org.sisioh.dddbase.core

/**
 * 汎用的な非同期型オンメモリ不変リポジトリ。
 *
 * @param core 内部で利用するオンメモリ不変リポジトリ。
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
class GenericAsyncOnMemoryImmutableRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
(protected val core: GenericOnMemoryImmutableRepository[ID, T] = new GenericOnMemoryImmutableRepository[ID, T])
  extends AsyncOnMemoryRepository[GenericAsyncOnMemoryImmutableRepository[ID, T], GenericOnMemoryImmutableRepository[ID, T], ID, T] {

  protected def createInstance(state: GenericOnMemoryImmutableRepository[ID, T]): GenericAsyncOnMemoryImmutableRepository[ID, T] =
    new GenericAsyncOnMemoryImmutableRepository[ID, T](state)

}
