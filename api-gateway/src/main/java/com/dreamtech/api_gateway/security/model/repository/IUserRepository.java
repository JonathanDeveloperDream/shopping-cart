package com.dreamtech.api_gateway.security.model.repository;

import com.dreamtech.api_gateway.security.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    @Modifying
    @Query("UPDATE User u SET u.disabled = :disabled WHERE u.id = :id")
    void disabledAndEnabled(@Param("id") long id, @Param("disabled") boolean isDisabled);
    @Modifying

    boolean existsByUsername(String username);

    boolean existsById(Long id);
    Page<User> findAllByOrderByIdDesc(Pageable pageable);
}
