package org.sisioh.dddbase.core

import org.sisioh.dddbase.core.lifecycle.EntitiesChunk
import org.sisioh.dddbase.core.lifecycle.sync.SyncEntityReader
import org.sisioh.dddbase.core.model.{Identity, Entity}
import org.sisioh.dddbase.spec.Specification
import scala.util.Try

/**
 * [[org.sisioh.dddbase.spec.Specification]]を述語に取り、エンティティを検索することができるトレイト。
 *
 * @tparam ID 識別子の型
 * @tparam T エンティティの型
 */
trait SyncEntityReaderBySpecification[ID <: Identity[_], T <: Entity[ID]]
  extends EntityReaderBySpecification[ID, T, Try] {
  this: SyncEntityReader[ID, T] =>

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
  def filterBySpecification(specification: Specification[T], index: Option[Int] = None, maxEntities: Option[Int] = None): Try[EntitiesChunk[ID, T]]

}
