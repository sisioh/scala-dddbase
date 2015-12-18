package org.sisioh.dddbase.lifecycle.memory.mutable.async

import java.util.UUID
import org.sisioh.dddbase.core.model.Identifier

class TestAsyncRepositoryOnMemory
    extends AbstractAsyncRepositoryOnMemory[Identifier[UUID], TestEntity]
    with TestAsyncRepository {

  type This = TestAsyncRepositoryOnMemory

}
