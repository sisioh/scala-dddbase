package org.sisioh.dddbase.core.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.model.Identity

trait TestAsyncRepository
  extends AsyncRepository[Identity[UUID], TestEntity] {
}
