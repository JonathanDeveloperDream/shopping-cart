package com.dreamtech.api_gateway.security.controller;

import com.dreamtech.api_gateway.security.Util.JwtUtil;
import com.dreamtech.api_gateway.security.dto.LoginDto;
import com.dreamtech.api_gateway.security.dto.RegisterUserDto;
import com.dreamtech.api_gateway.security.model.entities.User;
import com.dreamtech.api_gateway.security.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService; //  Agregar UserService

    public AuthController(ReactiveAuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserService userService) { //  Inyectar UserService
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, Object>>> login(@RequestBody @Valid LoginDto loginDto) {

        return userService.findByUsername(loginDto.getUsername())
                .flatMap(userDetails -> {

                    Map<String, Object> userExtraData = userService.getUser(loginDto.getUsername());

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    loginDto.getUsername(),
                                    loginDto.getPassword()
                            );

                    return authenticationManager.authenticate(authToken)
                            .flatMap(authentication -> {
                                String jwt = jwtUtil.createToken(userDetails);

                                Map<String, Object> response = new HashMap<>();
                                response.put("status", "Login successful");
                                response.put("message", "¡Bienvenido " + loginDto.getUsername() + "!");
                                response.put("email", userExtraData.get("email"));
                                response.put("token", jwt);

                                // Llamamos al servicio para cambiar el estado a true
                                return userService.updateSessionStatus(loginDto.getUsername(), true)
                                        .thenReturn(ResponseEntity.ok()
                                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                                                .body(response));
                            });
                })
                .onErrorResume(e -> {

                    if (e instanceof DisabledException) {
                        return Mono.error(new IllegalArgumentException("El usuario está deshabilitado"));
                    }
                    if (e instanceof IllegalArgumentException) {
                        return Mono.error(new IllegalArgumentException(e.getMessage()));
                    }
                    if (e instanceof BadCredentialsException || e instanceof UsernameNotFoundException) {
                        return Mono.error(new IllegalArgumentException("Usuario o contraseña incorrectos"));
                    }

                    return Mono.error(new RuntimeException("Error en el login: " + e.getMessage()));
                });

    }
    //   endpoint de registro
    @PostMapping("/register")
    public Mono<ResponseEntity<Map<String, Object>>> register(@RequestBody @Valid RegisterUserDto registerDto) {
        return userService.save(registerDto)
                .map(savedUser -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Usuario registrado exitosamente");
                    response.put("username", savedUser.getUsername());
                    response.put("email", savedUser.getEmail());

                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    System.err.println("❌ Error en registro: " + e.getMessage());

                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("status", "error");
                    errorResponse.put("message", "Error al registrar usuario: " + e.getMessage());

                    return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                });
    }
}