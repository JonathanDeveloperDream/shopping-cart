package com.dreamtech.api_gateway.security.config;

import com.auth0.jwt.JWT;
import com.dreamtech.api_gateway.security.Util.JwtUtil;
import com.dreamtech.api_gateway.security.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter implements WebFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;


    public JwtFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        // 🔥 1. TOKEN EXPIRADO → CERRAR SESIÓN
        if (jwtUtil.isTokenExpired(token)) {
            // Usamos el nuevo método que no valida expiración
            String username = this.getUsernameExpired(token);

            return userService.updateSessionStatus(username, false)
                    .then(Mono.defer(() -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }));
        }

        // 🔥 2. TOKEN INVÁLIDO
        if (!jwtUtil.isValidToken(token)) {
            return chain.filter(exchange);
        }

        // ✅ 3. TOKEN OK → AUTENTICAR
        String username = jwtUtil.getUsername(token);
        List<String> roles = jwtUtil.getRolesFromToken(token);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        roles.stream()
                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                                .toList()
                );

        return chain.filter(exchange)
                .contextWrite(
                        ReactiveSecurityContextHolder.withAuthentication(authentication)
                );
    }

    public String getUsernameExpired(String jwt) {
        return JWT.decode(jwt).getSubject();
    }
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return chain.filter(exchange);
//        }
//
//        String token = authHeader.substring(7);
//
//        try {
//            if (!jwtUtil.isValidToken(token)) {
//                return chain.filter(exchange);
//            }
//
//            String username = jwtUtil.getUsername(token);
//            List<String> roles = jwtUtil.getRolesFromToken(token); // Extraer roles
//
//            // 🔍 DEBUG - Eliminar después de probar
//            System.out.println("🔐 Username: " + username);
//            System.out.println("👤 Roles: " + roles);
//
//            // Convertir a GrantedAuthority con prefijo ROLE_
//            List<GrantedAuthority> authorities = roles.stream()
//                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                    .collect(Collectors.toList());
//
//            System.out.println("🎫 Authorities: " + authorities);
//
//            Authentication authentication = new UsernamePasswordAuthenticationToken(
//                    username,
//                    null,
//                    authorities
//            );
//
//            return chain.filter(exchange)
//                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
//
//        } catch (Exception e) {
//            System.err.println("❌ Error: " + e.getMessage());
//            e.printStackTrace();
//            return chain.filter(exchange);
//        }
//    }
}