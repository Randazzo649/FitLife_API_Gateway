
package com.fitlife.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class TreinoRequestFilter
        extends AbstractGatewayFilterFactory<TreinoRequestFilter.Config> {

    public TreinoRequestFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String origem = exchange.getRequest()
                    .getHeaders()
                    .getFirst("X-FitLife-Client");

            if (origem == null || origem.isBlank()) {
                exchange.getResponse()
                        .setStatusCode(HttpStatus.BAD_REQUEST);

                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}

