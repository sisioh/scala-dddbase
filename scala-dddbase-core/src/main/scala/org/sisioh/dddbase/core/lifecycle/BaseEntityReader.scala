package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.language.higherKinds

/**
 * [[org.sisioh.dddbase.core.model.Identity]]を用いて
 * [[org.sisioh.dddbase.core.model.Entity]]
 * を読み込むための責務を表す基本トレイト。
 *
 * @tparam ID 識別子の型
 * @tparam E エンティティの型
 * @tparam M モナド
 */
trait BaseEntityReader[ID <: Identity[_], E <: Entity[ID], M[+A]]
  extends EntityIO {

  type This <: BaseEntityReader[ID, E, M]

  protected def resolve(identity: ID)(implicit ctx: EntityIOContext[M]): M[E]

  /**
   * 複数の値からエンティティを取得し、`M[Seq[E]]` に変換する。
   *
   * @param values 入力値の集合
   * @param f エンティティを引き当てるための関数
   * @param ctx [[org.sisioh.dddbase.core.lifecycle.EntityIOContext]]
   * @tparam V 入力値の型
   * @return M にラップされた Seq[E]
   */
  protected def traverse[V](values: Seq[V])(f: (V) => M[E])
                           (implicit ctx: EntityIOContext[M]): M[Seq[E]]

  protected def resolves(identities: Seq[ID])(implicit ctx: EntityIOContext[M]): M[Seq[E]]

  protected def apply(identity: ID)(implicit ctx: EntityIOContext[M]): M[E]

  protected def containsByIdentity(identity: ID)(implicit ctx: EntityIOContext[M]): M[Boolean]

  protected def containsByIdentities(identities: Seq[ID])(implicit ctx: EntityIOContext[M]): M[Boolean]

  protected def contains(entity: E)(implicit ctx: EntityIOContext[M]): M[Boolean]

  protected def contains(entities: Seq[E])(implicit ctx: EntityIOContext[M]): M[Boolean]

}
