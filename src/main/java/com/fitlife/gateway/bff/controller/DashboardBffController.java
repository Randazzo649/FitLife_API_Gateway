package com.fitlife.gateway.bff.controller;

import com.fitlife.gateway.bff.dto.DashboardResponse;
import com.fitlife.gateway.bff.service.DashboardBffService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/bff")
public class DashboardBffController {

    private final DashboardBffService dashboardBffService;

    public DashboardBffController(
            DashboardBffService dashboardBffService) {

        this.dashboardBffService = dashboardBffService;
    }

    @GetMapping("/dashboard/{usuarioId}")
    public Mono<DashboardResponse> buscarDashboard(
            @PathVariable String usuarioId) {

        return dashboardBffService.buscarDashboard(usuarioId);
    }
}