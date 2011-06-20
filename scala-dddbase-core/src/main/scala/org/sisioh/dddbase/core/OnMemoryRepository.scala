package org.sisioh.dddbase.core

import collection.Iterator

/** オンメモリで動作するリポジトリの実装。
 *
 *  @author j5ik2o
 */
@cloneable
class OnMemoryRepository[T <: Entity with EntityCloneable[T]]
    extends Repository[T] {

  private[core] var entities = collection.mutable.Map.empty[Identifier, T]

  override def equals(obj: Any) = obj match {
    case that: OnMemoryRepository[_] => this.entities == that.entities
    case _ => false
  }

  override def hashCode = entities.hashCode

  override def clone: OnMemoryRepository[T] = {
    val result = super.clone.asInstanceOf[OnMemoryRepository[T]]
    val _entities = collection.mutable.Map.empty[Identifier, T]
    result.entities.foreach(e => _entities += (e._1 -> e._2.clone))
    result.entities = _entities
    result
  }

  def resolve(identifier: Identifier) = {
    require(identifier != null)
    if (contains(identifier) == false) {
      throw new EntityNotFoundException()
    }
    entities(identifier).clone
  }

  def store(entity: T) =
    entities += (entity.identifier -> entity)

  def delete(identifier: Identifier) = {
    if (contains(identifier) == false) {
      throw new EntityNotFoundException()
    }
    entities -= identifier
  }

  def delete(entity: T) =
    delete(entity.identifier)

  def iterator: Iterator[T] = entities.map(_._2.clone).iterator
}
