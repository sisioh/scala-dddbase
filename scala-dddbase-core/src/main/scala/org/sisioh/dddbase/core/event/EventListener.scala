package org.sisioh.dddbase.core.event

/**
 * イベントの受信先を表すトレイト。
 * @author j5ik2o
 */
trait EventListener {

  def handleEvent(event: Event)

}