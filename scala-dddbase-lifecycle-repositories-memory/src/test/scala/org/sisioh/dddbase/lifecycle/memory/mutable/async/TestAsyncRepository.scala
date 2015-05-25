package org.sisioh.dddbase.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.lifecycle.async.AsyncRepository
import org.sisioh.dddbase.core.model.Identifier

trait TestAsyncRepository
    extends AsyncRepository[Identifier[UUID], TestEntity] {

}
