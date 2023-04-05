package org.example.hello

import com.linecorp.armeria.server.ServiceRequestContext
import org.example.hello.schema.resources.HelloReply
import org.example.hello.schema.resources.HelloRequest
import org.example.hello.schema.resources.HelloServiceGrpcKt
import org.example.hello.schema.resources.helloReply
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class HelloController(
    private val helloService: HelloService
) : HelloServiceGrpcKt.HelloServiceCoroutineImplBase() {
    @Transactional
    override suspend fun helloWithTransaction(request: HelloRequest): HelloReply {
        // Thread[reactor-tcp-nio-1,5,main] expected
        println(Thread.currentThread())
        // null expected
        println(ServiceRequestContext.currentOrNull())
        return helloReply {
            message = "Hello, ${request.name}!"
        }
    }

    override suspend fun helloWithoutTransaction(request: HelloRequest): HelloReply {
        // Thread[armeria-common-worker-nio-2-3,5,main] expected
        println(Thread.currentThread())
        // not null expected
        println(ServiceRequestContext.currentOrNull())
        return helloReply {
            message = "Hello, ${request.name}!"
        }
    }

    override suspend fun helloWithInnerTransaction(request: HelloRequest): HelloReply {
        // Thread[armeria-common-worker-nio-2-3,5,main] expected
        println(Thread.currentThread())
        // not null expected
        println(ServiceRequestContext.currentOrNull())
        return helloReply {
            message = helloService.getHelloReply()
        }
    }
}
