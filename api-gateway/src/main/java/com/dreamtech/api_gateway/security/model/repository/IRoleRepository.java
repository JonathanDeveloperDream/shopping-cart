package com.dreamtech.api_gateway.security.model.repository;

import com.dreamtech.api_gateway.security.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);


}
