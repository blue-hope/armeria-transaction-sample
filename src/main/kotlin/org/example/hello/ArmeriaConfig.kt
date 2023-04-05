package org.example.hello

import com.linecorp.armeria.common.grpc.GrpcJsonMarshaller
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.docs.DocServiceFilter
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.grpc.GrpcServiceBuilder
import com.linecorp.armeria.server.grpc.HttpJsonTranscodingOptions
import com.linecorp.armeria.server.grpc.HttpJsonTranscodingQueryParamMatchRule
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import io.grpc.BindableService
import io.grpc.health.v1.HealthGrpc
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.reflection.v1alpha.ServerReflectionGrpc
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ArmeriaConfig {
    @Bean
    open fun armeriaServerConfigurator(
        bindableServices: List<BindableService>,
    ): ArmeriaServerConfigurator = ArmeriaServerConfigurator { serverBuilder ->
        serverBuilder.service(
            GrpcService.builder()
                .addExceptionMapping(Throwable::class.java, StatusFunction())
                .enableHealthCheckService(true)
                .enableUnframedRequests(true)
                .enableHttpJsonTranscoding(
                    HttpJsonTranscodingOptions.builder().build()
                )
                .jsonMarshallerFactory { serviceDescriptor ->
                    GrpcJsonMarshaller.builder().build(serviceDescriptor)
                }
                .intercept()
                .addService(ProtoReflectionService.newInstance())
                .apply {
                    bindableServices.forEach { addService(it) }
                }.build(),
            listOf()
        ).serviceUnder(
            "/docs",
            DocService.builder()
                .exclude(
                    DocServiceFilter.ofServiceName(ServerReflectionGrpc.SERVICE_NAME)
                        .or(DocServiceFilter.ofServiceName(HealthGrpc.SERVICE_NAME))
                )
                .build()
        )
            .requestTimeoutMillis(60000)
            .gracefulShutdownTimeoutMillis(5000, 5000)
    }
}
