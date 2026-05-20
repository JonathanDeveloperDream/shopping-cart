package com.dreamtech.api_gateway.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
//@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(exchanges -> exchanges

                                //Público

                                .pathMatchers(

                                        "/swagger-ui.html",
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",

                                        "/customer-service/api/v1/customer/v3/api-docs/**",
                                        "/order-service/api/v1/order/v3/api-docs/**",
                                        "/order-detail-service/api/v1/order-detail/v3/api-docs/**"

                                ).permitAll()
                                .pathMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                                .pathMatchers(HttpMethod.POST, "/api/auth/register").permitAll()

                                //ENDPOINTS OF USERS
                                .pathMatchers(HttpMethod.GET, "/api/users/**").permitAll()

                                .pathMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMINISTRATOR")
                                .pathMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMINISTRATOR")
                                .pathMatchers(HttpMethod.PATCH, "/api/users/**").hasRole("ADMINISTRATOR")

                                .pathMatchers(HttpMethod.GET, "/customer-service/api/v1/customer/customers/hello").hasRole("CUSTOMER")
                                .pathMatchers(HttpMethod.GET, "/customer-service/api/v1/customer/customers/**").hasRole("CUSTOMER")
                                .pathMatchers(HttpMethod.POST, "/customer-service/api/v1/customer/customers/**").hasRole("CUSTOMER")

                                .pathMatchers(HttpMethod.GET, "/order-service/api/v1/order/orders/**").hasRole("CUSTOMER")
                                .pathMatchers(HttpMethod.POST, "/order-service/api/v1/order/orders/**").hasRole("CUSTOMER")
                                .pathMatchers(HttpMethod.PUT, "/order-service/api/v1/order/orders/**").hasRole("CUSTOMER")
                                .pathMatchers(HttpMethod.GET, "/order-detail-service/api/v1/order-detail/order-details/**").hasRole("CUSTOMER")
                                .pathMatchers(HttpMethod.POST, "/order-detail-service/api/v1/order-detail/order-details/**").hasRole("CUSTOMER")
                                .pathMatchers(HttpMethod.GET, "/payment-service/api/v1/payment/payments/**").hasRole("CUSTOMER")
                                .pathMatchers(HttpMethod.POST, "/payment-service/api/v1/payment/payments/**").hasRole("CUSTOMER")

//                        //TODO LO DEMAS BLOQUEADO
                                .anyExchange().denyAll()
                )
                .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(
            ReactiveUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        UserDetailsRepositoryReactiveAuthenticationManager authManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);

        authManager.setPasswordEncoder(passwordEncoder);

        return authManager;
    }

}
