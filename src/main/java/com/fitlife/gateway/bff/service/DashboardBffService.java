package com.fitlife.gateway.bff.service;

import com.fitlife.gateway.bff.client.TreinoServiceClient;
import com.fitlife.gateway.bff.client.UsuarioServiceClient;
import com.fitlife.gateway.bff.dto.DashboardResponse;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class DashboardBffService {

    private final UsuarioServiceClient usuarioClient;
    private final TreinoServiceClient treinoClient;

    public DashboardBffService(
            UsuarioServiceClient usuarioClient,
            TreinoServiceClient treinoClient) {

        this.usuarioClient = usuarioClient;
        this.treinoClient = treinoClient;
    }

    public Mono<DashboardResponse> buscarDashboard(String usuarioId) {

        Mono<com.fasterxml.jackson.databind.JsonNode> usuario =
                usuarioClient.buscarPorId(usuarioId);

        Mono<com.fasterxml.jackson.databind.JsonNode> treinos =
                treinoClient.buscarPorUsuario(usuarioId);

        return Mono.zip(usuario, treinos)
                .map(resultado -> new DashboardResponse(
                        resultado.getT1(),
                        resultado.getT2()
                ));
    }
}