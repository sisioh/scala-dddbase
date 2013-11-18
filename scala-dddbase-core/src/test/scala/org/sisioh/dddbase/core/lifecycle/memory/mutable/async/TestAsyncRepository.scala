package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.model.Identity
import scala.concurrent.Future
import org.sisioh.dddbase.core.lifecycle.EntityIOContext

trait TestAsyncRepository
  extends AsyncRepository[EntityIOContext[Future], Identity[UUID], TestEntity] {

}
