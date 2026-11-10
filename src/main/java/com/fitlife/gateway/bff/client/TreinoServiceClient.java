
package com.fitlife.gateway.bff.client;

import com.fasterxml.jackson.databind.JsonNode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServerBearerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class TreinoServiceClient {

    private final WebClient webClient;

    public TreinoServiceClient(
            WebClient.Builder builder,
            @Value("${fitlife.services.treino.base-url}") String baseUrl) {

        this.webClient = builder.clone()
                .baseUrl(baseUrl)
                .filter(new ServerBearerExchangeFilterFunction())
                .build();
    }

    public Mono<JsonNode> buscarPorUsuario(String usuarioId) {
        return webClient.get()
                .uri("/api/treinos/usuario/{id}", usuarioId)
                .retrieve()
                .bodyToMono(JsonNode.class);
    }
}