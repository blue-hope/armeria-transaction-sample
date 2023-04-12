package org.example

import com.linecorp.armeria.common.reactor3.RequestContextHooks
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class HelloApp

fun main(args: Array<String>) {
    RequestContextHooks.enable()
    runApplication<HelloApp>(*args)
}
