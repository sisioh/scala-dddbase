package org.sisioh.dddbase.core

import scala.util.Try

/**
 * オンメモリで動作する可変リポジトリの実装。
 *
 * @tparam R 当該リポジトリを実装する派生型
 * @tparam ID エンティティの識別子の型
 * @tparam T エンティティの型
 */
trait OnMemoryMutableRepository
[R <: OnMemoryMutableRepository[R, ID, T],
ID <: Identity[_],
T <: Entity[ID] with EntityCloneable[ID, T]]
  extends OnMemoryRepository[R, ID, T] {

  protected var core: OnMemoryRepository[_, ID, T] =
    new GenericOnMemoryImmutableRepository[ID, T]()

  override def equals(obj: Any) = obj match {
    case that: OnMemoryMutableRepository[_, _, _] =>
      this.core == that.core
    case _ => false
  }

  override def hashCode = 31 * core.hashCode()

  def store(entity: T): Try[R] = {
    core.store(entity).map {
      result =>
        core = result.asInstanceOf[OnMemoryRepository[_, ID, T]]
        this.asInstanceOf[R]
    }
  }

  def delete(identity: ID): Try[R] = {
    core.delete(identity).map {
      result =>
        core = result.asInstanceOf[OnMemoryRepository[_, ID, T]]
        this.asInstanceOf[R]
    }
  }

  def resolveOption(identity: ID): Try[Option[T]] = core.resolveOption(identity)

  def iterator: Iterator[T] = core.iterator

  def resolve(identity: ID): Try[T] = core.resolve(identity)

}
