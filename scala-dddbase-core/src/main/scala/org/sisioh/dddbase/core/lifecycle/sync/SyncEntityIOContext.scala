package org.sisioh.dddbase.core.lifecycle.sync

import org.sisioh.dddbase.core.lifecycle.EntityIOContext
import scala.util.Try

trait SyncEntityIOContext extends EntityIOContext[Try]

object SyncEntityIOContext extends SyncEntityIOContext
