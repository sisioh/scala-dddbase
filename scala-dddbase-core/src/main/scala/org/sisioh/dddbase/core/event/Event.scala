package org.sisioh.dddbase.core.event

import org.sisioh.dddbase.core.Identifier

/**
 * Created by IntelliJ IDEA.
 * User: junichi
 * Date: 11/04/18
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */

trait Event {

  val identifier: Identifier

  val timestamp: Long

}