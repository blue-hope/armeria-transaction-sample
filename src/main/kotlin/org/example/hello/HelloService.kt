package org.example.hello

import com.linecorp.armeria.server.ServiceRequestContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class HelloService {
    @Transactional
    open suspend fun getHelloReply(): String {
        // Thread[reactor-tcp-nio-1,5,main] expected
        println(Thread.currentThread())
        // null expected
        println(ServiceRequestContext.currentOrNull())
        return "Hello, World!"
    }
}
