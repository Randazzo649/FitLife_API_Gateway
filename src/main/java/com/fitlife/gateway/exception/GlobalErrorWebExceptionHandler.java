
package com.fitlife.gateway.exception;

import com.fitlife.gateway.exception.dto.GatewayErrorResponse;

import java.net.ConnectException;
import java.time.Instant;
import java.net.UnknownHostException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler
        extends AbstractErrorWebExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorWebExceptionHandler.class);

    public GlobalErrorWebExceptionHandler(
            ErrorAttributes errorAttributes,
            WebProperties webProperties,
            ApplicationContext applicationContext,
            ServerCodecConfigurer serverCodecConfigurer) {

        super(
                errorAttributes,
                webProperties.getResources(),
                applicationContext);

        setMessageReaders(serverCodecConfigurer.getReaders());
        setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(
            ErrorAttributes errorAttributes) {

        return RouterFunctions.route(
                RequestPredicates.all(),
                this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(
            ServerRequest request) {

        Throwable exception = getError(request);

        if (exception == null) {
            exception = new IllegalStateException("Unknown gateway error");
        }

        HttpStatusCode status = determineStatus(exception);

        logger.error(
                "Gateway error: {} {} - HTTP {}",
                request.method(),
                request.path(),
                status.value(),
                exception);

        GatewayErrorResponse response = new GatewayErrorResponse(
                Instant.now(),
                status.value(),
                resolveErrorName(status),
                resolveMessage(status),
                request.path());

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }

    private HttpStatusCode determineStatus(Throwable exception) {

        Throwable current = exception;

        while (current != null) {

            if (current instanceof org.springframework.cloud.gateway.support.TimeoutException
                    || current instanceof TimeoutException
                    || current instanceof SocketTimeoutException) {
                return HttpStatus.GATEWAY_TIMEOUT;
            }

            if (current instanceof WebClientResponseException responseException) {
                return responseException.getStatusCode();
            }

            if (current instanceof ResponseStatusException responseException) {
                return responseException.getStatusCode();
            }

            if (current instanceof ConnectException
                    || current instanceof UnknownHostException) {
                return HttpStatus.SERVICE_UNAVAILABLE;
            }

            current = current.getCause();
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String resolveErrorName(HttpStatusCode status) {

        HttpStatus resolved = HttpStatus.resolve(status.value());

        return resolved != null
                ? resolved.getReasonPhrase()
                : "Gateway Error";
    }

    private String resolveMessage(HttpStatusCode status) {

        return switch (status.value()) {
            case 404 -> "Recurso nao encontrado.";
            case 503 -> "Servico temporariamente indisponivel.";
            case 504 -> "Tempo limite de comunicacao excedido.";
            default -> "Nao foi possivel processar a solicitacao.";
        };
    }
}