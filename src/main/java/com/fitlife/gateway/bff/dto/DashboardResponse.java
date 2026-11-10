package com.fitlife.gateway.bff.dto;

import com.fasterxml.jackson.databind.JsonNode;

public record DashboardResponse(
        JsonNode usuario,
        JsonNode treinos
) {
}