package com.dreamtech.api_gateway.security.controller;

import com.dreamtech.api_gateway.security.dto.UserResponseDto;
import com.dreamtech.api_gateway.security.model.entities.User;
import com.dreamtech.api_gateway.security.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/{userId}/roles")
    public Mono<ResponseEntity<Map<String, Object>>> assignRoleToUser(
            @PathVariable Long userId,
            @RequestBody Map<String, String> request) {

        String roleName = request.get("role");
        return userService.assignRole(userId, roleName)
                .map(user -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Rol asignado exitosamente");
                    response.put("username", user.getUsername());

                    return ResponseEntity.ok(response);
                })
                .onErrorResume(e -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("status", "error");
                    errorResponse.put("message", e.getMessage());

                    return Mono.just(ResponseEntity.badRequest().body(errorResponse));
                });
    }
    @GetMapping
    public Mono<Page<UserResponseDto>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponseDto> result = userService.getUsers(pageable);

        return Mono.just(result);
    }
    @GetMapping("/roles")
    public Mono<ResponseEntity<?>> getRoles() {
        return Mono.just(ResponseEntity.ok(userService.getRoles()));

    }
    @PutMapping("/status-account/{id}")
    public Mono<ResponseEntity<String>> changeStatusAccount(@RequestParam boolean isDisabled, @PathVariable long id) {
        return Mono.fromCallable(() -> {
                    // Si esto lanza Exception, irá al onErrorResume
                    userService.disableAndEnableAccount(id, isDisabled);
                    return "Estado de cuenta actualizado con éxito";
                })
                .subscribeOn(Schedulers.boundedElastic())
                .map(message -> ResponseEntity.ok(message))
                .onErrorResume(e -> {
                    // Esto te ayudará a ver el error real en la consola
                    System.err.println("Error en changeStatusAccount: " + e.getMessage());
                    e.printStackTrace();

                    return Mono.just(ResponseEntity.status(500)
                            .body("Error al actualizar cuenta: " + e.getMessage()));
                });
    }

    @PatchMapping("/{userId}/delete-role")
    public Mono<ResponseEntity<String>> deleteUserRoleByUserIs(@PathVariable Long userId) {
        return Mono.fromCallable(() -> userService.deleteUserRoleByUserIs(userId))
                .subscribeOn(Schedulers.boundedElastic())
                .map(message -> ResponseEntity.ok(message))
                .onErrorResume(e -> {

                    System.err.println("Error al eliminar roles: " + e.getMessage());

                    return Mono.just(ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("No se pudo eliminar el rol: " + e.getMessage()));
                });
    }
    @GetMapping("/existe-user-id")
    public ResponseEntity<?> isExisteUserById(@RequestParam Long id){
        return ResponseEntity.ok(userService.isUserExistById(id));
    }

    @GetMapping("/user-id")
    public ResponseEntity<?> getUserById(@RequestParam Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
