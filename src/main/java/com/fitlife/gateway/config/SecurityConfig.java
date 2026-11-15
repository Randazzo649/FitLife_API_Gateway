package com.fitlife.gateway.config;
import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;import org.springframework.http.HttpMethod;import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;import org.springframework.security.config.web.server.ServerHttpSecurity;import org.springframework.security.web.server.SecurityWebFilterChain;
@Configuration @EnableWebFluxSecurity public class SecurityConfig {
 @Bean public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){return http.csrf(ServerHttpSecurity.CsrfSpec::disable).authorizeExchange(ex->ex.pathMatchers(HttpMethod.OPTIONS,"/**").permitAll().pathMatchers("/oauth2/jwks", "/uploads/**").permitAll().pathMatchers(HttpMethod.POST,"/api/usuarios/login","/api/usuarios/cadastro").permitAll().anyExchange().authenticated()).oauth2ResourceServer(o->o.jwt(jwt->{})).build();}
}
