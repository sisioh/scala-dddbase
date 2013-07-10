package org.sisioh.dddbase.event

import org.sisioh.dddbase.core.model.{Entity, Identity}

/**
 * ドメインイベントを表すエンティティ。
 */
trait DomainEvent[ID <: Identity[_]] extends Entity[ID]
