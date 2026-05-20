package com.dreamtech.api_gateway.security.service;

import com.dreamtech.api_gateway.security.config.SecurityConfig;
import com.dreamtech.api_gateway.security.dto.RegisterUserDto;
import com.dreamtech.api_gateway.security.dto.UserResponseDto;
import com.dreamtech.api_gateway.security.model.entities.Role;
import com.dreamtech.api_gateway.security.model.entities.User;
import com.dreamtech.api_gateway.security.model.repository.IRoleRepository;
import com.dreamtech.api_gateway.security.model.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService implements ReactiveUserDetailsService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Cambiado de SecurityConfig a PasswordEncoder
    private final IRoleRepository roleRepository;


    public UserService(IUserRepository userRepository, IRoleRepository roleRepository,
                       PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromCallable(() -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));


            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                    .collect(Collectors.toList());

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(authorities)
                    .disabled(user.isDisabled())
                    .accountLocked(user.isLocked())
                    .build();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Transactional
    public Mono<Void> updateSessionStatus(String username, boolean status) {
        return Mono.fromRunnable(() -> {
            userRepository.findByUsername(username).ifPresent(user -> {

                userRepository.save(user);
            });
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    public Map<String,Object> getUser(String username){
        Map<String,Object> response = new HashMap<>();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no existe"));
        response.put("username",user.getUsername());
        response.put("email",user.getEmail());
        return response;

    }
    public Mono<RegisterUserDto> save(RegisterUserDto userDto) {
        return Mono.fromCallable(() -> {
            boolean userExists = userRepository.existsByUsername(userDto.getUsername());
            if (userExists) {
                throw new IllegalArgumentException("El nombre de usuario ya está en uso");
            }
            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }
            User user = new User();
            user.setUsername(userDto.getUsername());


            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            user.setEmail(userDto.getEmail());
            user.setLocked(false);
            user.setDisabled(false);

            userRepository.save(user);
            return userDto;
        }).subscribeOn(Schedulers.boundedElastic());
    }
    public Mono<RegisterUserDto> assignRole(Long userId, String roleName) {
        return Mono.fromCallable(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario con ID: " + userId));

            if (!user.getRoles().isEmpty()) {
                throw new IllegalArgumentException("El usuario ya tiene un rol asignado");
            }

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("No existe el rol: " + roleName));


            user.getRoles().add(role);

            User savedUser = userRepository.save(user);

            RegisterUserDto userDto = new RegisterUserDto();
            userDto.setUsername(savedUser.getUsername());
            userDto.setEmail(savedUser.getEmail());

            return userDto;
        }).subscribeOn(Schedulers.boundedElastic());
    }


    public Page<UserResponseDto> getUsers(Pageable pageable) {

        Page<User> users = userRepository.findAllByOrderByIdDesc(pageable);
        List<UserResponseDto> usersDto = users.getContent().stream()
                .map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .locked(user.isLocked())
                        .disabled(user.isDisabled())
                        .roles(user.getRoles())
                        .build()

                ).collect(Collectors.toList());
        return new PageImpl<>(usersDto,pageable,users.getTotalElements());

    }

    public List<Role> getRoles (){
        return roleRepository.findAll();
    }

    @Transactional
    public String disableAndEnableAccount (long id, boolean isDisabled){
        userRepository.disabledAndEnabled(id,isDisabled);
        return isDisabled ? "Desactivado" : "Activado";
    }

    @Transactional
    public String deleteUserRoleByUserIs(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario con ID: " + userId));
        user.getRoles().clear();
        user.setDisabled(true);
        userRepository.save(user);
        return "Role eliminado";

    }

    public boolean isUserExistById(Long id){
        return userRepository.existsById(id);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("No existe el usuario con ID: " + id));
    }

}