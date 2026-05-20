package com.tecnoin.customer_service.Repository;

import com.tecnoin.customer_service.model.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Hidden
@FeignClient(name = "API-GATEWAY", path = "/api/users" )
public interface IUserServiceAPIClient {
    @GetMapping("/existe-user-id")
    boolean isExisteUserById(@RequestParam("id") Long id);

    @GetMapping("/user-id")
    UserResponseDTO getUserById(@RequestParam("id") Long id);
}
