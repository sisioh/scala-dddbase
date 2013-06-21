package org.sisioh.dddbase.core

class GenericAsyncOnMemoryMutableRepository[ID <: Identity[_], T <: Entity[ID] with EntityCloneable[ID, T]]
(protected val core: GenericOnMemoryMutableRepository[ID, T] = new GenericOnMemoryMutableRepository[ID, T])
  extends AsyncOnMemoryRepository[GenericAsyncOnMemoryMutableRepository[ID, T], GenericOnMemoryMutableRepository[ID, T], ID, T] {

  protected def createInstance(state: GenericOnMemoryMutableRepository[ID, T]): GenericAsyncOnMemoryMutableRepository[ID, T] =
    new GenericAsyncOnMemoryMutableRepository[ID, T](state)
//  protected def createInstance(state: GenericOnMemoryMutableRepository[ID, T]): GenericAsyncOnMemoryRepository[ID, T] = ???
}
