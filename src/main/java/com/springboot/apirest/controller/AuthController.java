package com.springboot.apirest.controller;

import com.springboot.apirest.Dtos.LoginDTO;
import com.springboot.apirest.Dtos.UsuarioDTO;
import com.springboot.apirest.security.JwtUtil;
import com.springboot.apirest.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService servicioUsuario;

    @Autowired
    private AuthenticationManager manejadorDeAutenticacion;

    @Autowired
    private JwtUtil utilidadJwt;

    // Endpoint para Registrarse
    @PostMapping("/register")
    public ResponseEntity<String> registrarUsuario(@Valid @RequestBody UsuarioDTO dtoUsuario) {
        try {
            servicioUsuario.registrarUsuario(dtoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("¡Usuario registrado con éxito!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint para Iniciar Sesión (Login)
    @PostMapping("/login")
    public ResponseEntity<String> iniciarSesion(@Valid @RequestBody LoginDTO dtoLogin) {
        try {
            manejadorDeAutenticacion.authenticate(
                    new UsernamePasswordAuthenticationToken(dtoLogin.getCorreo(), dtoLogin.getContrasenia()));

            UserDetails detallesDelUsuario = servicioUsuario.loadUserByUsername(dtoLogin.getCorreo());
            String tokenGenerado = utilidadJwt.generarToken(detallesDelUsuario);

            return ResponseEntity.ok(tokenGenerado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }
}
