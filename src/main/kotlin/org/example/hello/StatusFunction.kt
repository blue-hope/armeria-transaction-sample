package org.example.hello

import com.linecorp.armeria.common.RequestContext
import com.linecorp.armeria.common.grpc.GrpcStatusFunction
import io.grpc.Metadata
import io.grpc.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class StatusFunction() : GrpcStatusFunction {
    override fun apply(ctx: RequestContext, throwable: Throwable, metadata: Metadata): Status {
        log.error(throwable.message, throwable)
        return Status.INTERNAL
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
}
