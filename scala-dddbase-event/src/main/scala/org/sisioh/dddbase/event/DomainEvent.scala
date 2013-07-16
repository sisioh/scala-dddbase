package org.sisioh.dddbase.event

import org.sisioh.dddbase.core.model.{Entity, Identity}

/**
 * ドメインイベントを表すエンティティ。
 *
 * 主に、ドメイン層のアグリゲート内で発生したイベントを表す。
 *
 * @tparam ID 識別子の型
 */
trait DomainEvent[ID <: Identity[_]] extends Entity[ID]
