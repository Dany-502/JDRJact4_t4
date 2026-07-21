package com.springboot.apirest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Clave secreta para encriptar los tokens
    private static final String CLAVE_SECRETA = "MiClaveSecretaSuperSeguraParaElProyectoDeSpringBoot2026";
    private final SecretKey llaveSecreta = Keys.hmacShaKeyFor(CLAVE_SECRETA.getBytes());

    // Extraemos el correo del token
    public String extraerCorreo(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    // Extraemos la fecha de expiración del token
    public Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodasLasClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extraerTodasLasClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(llaveSecreta)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean tokenExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }

    // Generamos el token cuando el usuario inicia sesión
    public String generarToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return crearToken(claims, userDetails.getUsername());
    }

    private String crearToken(Map<String, Object> claims, String correo) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(correo)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(llaveSecreta, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validamos que el token sea del usuario correcto y no haya expirado
    public Boolean validarToken(String token, UserDetails userDetails) {
        final String correo = extraerCorreo(token);
        return (correo.equals(userDetails.getUsername()) && !tokenExpirado(token));
    }
}
