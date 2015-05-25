package org.sisioh.dddbase.core

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import org.sisioh.dddbase.core.model.{ Identifier, Entity }
import org.sisioh.dddbase.spec.Specification
import scala.util.Try

/**
 * 同期I/O`EntityReadableBySpecification`
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait SyncEntityReadableBySpecification[ID <: Identifier[_], T <: Entity[ID]]
    extends EntityReadableBySpecification[ID, T, Try] {
  this: SyncEntityReader[ID, T] =>

  /**
   * `Specification` に該当したエンティティを取得する。
   *
   * @param specification `Specification`
   * @return Success:
   *         チャンク
   *         Failure:
   *         EntityNotFoundExceptionは、エンティティが見つからなかった場合
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def filterBySpecification(specification: Specification[T],
                            index: Option[Int] = None,
                            maxEntities: Option[Int] = None): Try[EntitiesChunk[ID, T]]

}
