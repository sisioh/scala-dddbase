package org.sisioh.dddbase.core

class GenericAsyncOnMemoryRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
(protected val core: GenericOnMemoryImmutableRepository[ID, T] = new GenericOnMemoryImmutableRepository[ID, T])
  extends AsyncOnMemoryRepository[GenericAsyncOnMemoryRepository[ID, T], GenericOnMemoryImmutableRepository[ID, T], ID, T] {

  protected def createInstance(state: GenericOnMemoryImmutableRepository[ID, T]): GenericAsyncOnMemoryRepository[ID, T] =
    new GenericAsyncOnMemoryRepository[ID, T](state)

}
