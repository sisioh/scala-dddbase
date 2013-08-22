package org.sisioh.dddbase.core.lifecycle

import org.sisioh.dddbase.core.model.{Entity, Identity}
import scala.language.higherKinds

/**
 * [[org.sisioh.dddbase.core.lifecycle.EntityWriter]]の新しい状態と複数のエンティティを保持する値オブジェクト。
 *
 * @tparam EW [[org.sisioh.dddbase.core.lifecycle.EntityWriter]]の型
 * @tparam ID エンティティの識別子の型
 * @tparam E エンティティの型
 * @tparam M モナドの型
 */
trait ResultWithEntities[+EW <: EntityWriter[ID, E, M], ID <: Identity[_], E <: Entity[ID], M[+A]]  {

  /**
   * 結果
   */
  val result: EW

  /**
   * エンティティ
   */
  val entities: Seq[E]

}
