/*
 * Copyright 2010 TRICREO, Inc. (http://tricreo.jp/)
 * Copyright 2011 Sisioh Project and others. (http://www.sisioh.org/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.sisioh.dddbase.core.lifecycle.sync

import org.sisioh.dddbase.core.lifecycle.{ResultWithEntities, EntityIOContext, EntityWriter}
import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.util.Try

/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて
 * [[org.sisioh.dddbase.core.model.Entity]]
 * を書き込むための責務を表すインターフェイス。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 */
trait SyncEntityWriter[CTX <: EntityIOContext[Try], ID <: Identity[_], E <: Entity[ID]]
  extends EntityWriter[CTX, ID, E, Try] {

  type This <: SyncEntityWriter[CTX, ID, E]

  /**
   * エンティティを保存する。
   *
   * @param entity 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンスと保存したエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def store(entity: E)(implicit ctx: CTX): Try[SyncResultWithEntity[This, CTX, ID, E]]

  /**
   * 複数のタスクを個々のタスクに分解して処理するためのユーティリティメソッド。
   *
   * @param tasks タスクの集合
   * @param processor タスクを処理する関数
   * @param ctx [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]
   * @tparam A タスクの型
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  protected final def forEachEntities[A](tasks: Seq[A])(processor: (This, A) => Try[SyncResultWithEntity[This, CTX, ID, E]])
                                  (implicit ctx: CTX): Try[SyncResultWithEntities[This, CTX,  ID, E]] = Try {
    val result = tasks.foldLeft[(This, Seq[E])]((this.asInstanceOf[This], Seq.empty[E])) {
      (resultWithEntities, task) =>
        val resultWithEntity = processor(resultWithEntities._1, task).get
        (resultWithEntity.result.asInstanceOf[This], resultWithEntities._2 :+ resultWithEntity.entity)
    }
    SyncResultWithEntities(result._1, result._2)
  }

  /**
   * 複数のエンティティを保存する。
   *
   * @param entities 保存する対象のエンティティ
   * @return Success:
   *         リポジトリインスタンスと保存されたエンティティ
   *         Failure
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def store(entities: Seq[E])(implicit ctx: CTX): Try[SyncResultWithEntities[This, CTX, ID, E]] =
    forEachEntities(entities) {
      (repository, entity) =>
        repository.store(entity).asInstanceOf[Try[SyncResultWithEntity[This, CTX, ID, E]]]
    }

  /**
   * 指定した識別子のエンティティを削除する。
   *
   * @param identity 識別子
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def deleteByIdentity(identity: ID)(implicit ctx: CTX): Try[SyncResultWithEntity[This, CTX, ID, E]]

  /**
   * 指定した複数の識別子のエンティティを削除する。
   *
   * @param identities 識別子
   * @return Success:
   *         リポジトリインスタンスと削除されたエンティティ
   *         Failure:
   *         RepositoryExceptionは、リポジトリにアクセスできなかった場合。
   */
  def deleteByIdentities(identities: Seq[ID])(implicit ctx: CTX): Try[ResultWithEntities[This, CTX, ID, E, Try]] =
    forEachEntities(identities) {
      (repository, identity) =>
        repository.deleteByIdentity(identity).asInstanceOf[Try[SyncResultWithEntity[This, CTX, ID, E]]]
    }

}
