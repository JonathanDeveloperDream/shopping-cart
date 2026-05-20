package com.dreamtech.api_gateway.security.Util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "4ndr0m3d4ppdr34ms";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET_KEY);

    //  Crear token con roles
    public String createToken(UserDetails userDetails) {
        // Extraer roles sin el prefijo ROLE_
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toList());

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuer("andromedappdreams")
                .withClaim("roles", roles) // CRÍTICO: Agregar roles al JWT
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                .sign(ALGORITHM);
    }

    //  Mantener método antiguo para compatibilidad (opcional)
    public String createToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withIssuer("andromedappdreams")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                .sign(ALGORITHM);
    }

    public boolean isValidToken(String jwt) {
        try {
            JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public String getUsername(String jwt) {
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getSubject();
    }

    // Extraer roles del JWT
    public List<String> getRolesFromToken(String jwt) {
        DecodedJWT decodedJWT = JWT.require(ALGORITHM)
                .build()
                .verify(jwt);

        return decodedJWT.getClaim("roles").asList(String.class);
    }
    public boolean isTokenExpired(String jwt) {
        try {
            DecodedJWT decodedJWT = JWT.require(ALGORITHM)
                    .build()
                    .verify(jwt);

            Date expiresAt = decodedJWT.getExpiresAt();
            return expiresAt.before(new Date());

        } catch (JWTVerificationException e) {
            // Si falla la verificación, trátalo como expirado
            return true;
        }
    }

}