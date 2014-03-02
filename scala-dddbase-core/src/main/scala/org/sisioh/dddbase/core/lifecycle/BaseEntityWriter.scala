package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.language.higherKinds

/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて
 * [[org.sisioh.dddbase.core.model.Entity]]
 * を書き込むための責務を表す基本トレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 * @tparam M モナド
 */
trait BaseEntityWriter[ID <: Identity[_], E <: Entity[ID], M[+A]]
  extends EntityIO {

  type This <: BaseEntityWriter[ID, E, M]

  /**
   * エンティティを保存する。
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  protected def store(entity: E)(implicit ctx: EntityIOContext[M]): M[ResultWithEntity[This, ID, E, M]]

  /**
   * 複数のエンティティを保存する。
   *
   * @param entities 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  protected def store(entities: Seq[E])(implicit ctx: EntityIOContext[M]): M[ResultWithEntities[This, ID, E, M]]


  /**
   * 更新メソッド。
   *
   * {{{
   *   entityWriter(identity) = entity
   * }}}
   *
   * @param identity 識別子
   * @param entity エンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  protected def update(identity: ID, entity: E)(implicit ctx: EntityIOContext[M])

  /**
   * 指定した識別子のエンティティを削除する。
   *
   * @param identity 識別子
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  protected def deleteByIdentity(identity: ID)(implicit ctx: EntityIOContext[M]): M[ResultWithEntity[This, ID, E, M]]

  /**
   * 指定した複数の識別子のエンティティを削除する。
   *
   * @param identities 識別子
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  protected def deleteByIdentities(identities: Seq[ID])(implicit ctx: EntityIOContext[M]): M[ResultWithEntities[This, ID, E, M]]

  /**
   * エンティティを削除する。
   *
   * @param entity エンティティ
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  protected def delete(entity: E)(implicit ctx: EntityIOContext[M]): M[ResultWithEntity[This, ID, E, M]]

  protected def delete(entities: Seq[E])(implicit ctx: EntityIOContext[M]): M[ResultWithEntities[This, ID, E, M]]

}
