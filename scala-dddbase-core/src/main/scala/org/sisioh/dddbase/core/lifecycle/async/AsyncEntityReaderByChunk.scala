package org.sisioh.dddbase.core.lifecycle.async

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.{EntityReaderByChunk, EntitiesChunk}

/**
 * 非同期版[[org.sisioh.dddbase.core.lifecycle.EntityReaderByChunk]]。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait AsyncEntityReaderByChunk[ID <: Identity[_], E <: Entity[ID]]
  extends EntityReaderByChunk[ID, E, Future] {
  this: AsyncEntityReader[ID, E] =>

  /**
   * エンティティをチャンク単位で検索する。
   *
   * @param index 検索するチャンクのインデックス
   * @param maxEntities 1チャンクの件数
   * @return Success:
   *         チャンク
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def resolveChunk(index: Int, maxEntities: Int): Future[EntitiesChunk[ID, E]]

}
