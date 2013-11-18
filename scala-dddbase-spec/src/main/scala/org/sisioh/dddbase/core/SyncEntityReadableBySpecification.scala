package org.sisioh.dddbase.core

import org.sisioh.dddbase.core.lifecycle.{EntityIOContext, EntitiesChunk}
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import org.sisioh.dddbase.core.model.{Identity, Entity}
import org.sisioh.dddbase.spec.Specification
import scala.util.Try

/**
 * 同期I/O[[org.sisioh.dddbase.core.EntityReadableBySpecification]]
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait SyncEntityReadableBySpecification[CTX <: EntityIOContext[Try], ID <: Identity[_], T <: Entity[ID]]
  extends EntityReadableBySpecification[ID, T, Try] {
  this: SyncEntityReader[CTX, ID, T] =>

  /**
   * [[org.sisioh.dddbase.spec.Specification]]に該当したエンティティを取得する。
   *
   * @param specification [[org.sisioh.dddbase.spec.Specification]]
   * @return Success:
   *         チャンク
   *         Failure:
   *         EntityNotFoundExceptionは、エンティティが見つからなかった場合
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def filterBySpecification
  (specification: Specification[T],
   index: Option[Int] = None,
   maxEntities: Option[Int] = None): Try[EntitiesChunk[ID, T]]

}
