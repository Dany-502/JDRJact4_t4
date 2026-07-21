package com.springboot.apirest.security;

import com.springboot.apirest.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain cadenaFiltros)
            throws ServletException, IOException {

        // Primero obtenemos la cabecera "Authorization" de la petición HTTP
        final String cabeceraAutorizacion = request.getHeader("Authorization");

        String correoUsuario = null;
        String jwtToken = null;

        // Si la cabecera existe y empieza con Bearer, el token se extrae en esta parte
        // :P
        if (cabeceraAutorizacion != null && cabeceraAutorizacion.startsWith("Bearer ")) {
            jwtToken = cabeceraAutorizacion.substring(7);
            try {
                correoUsuario = jwtUtil.extraerCorreo(jwtToken);
            } catch (Exception e) {
                System.out.println("Error al procesar el token JWT: " + e.getMessage());
            }
        }

        // Si sacamos el correo del token y el usuario aún no está autenticado en Spring
        if (correoUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargamos el usuario de la base de datos
            UserDetails userDetails = this.usuarioService.loadUserByUsername(correoUsuario);

            // Validamos que el token sea correcto para este usuario
            if (jwtUtil.validarToken(jwtToken, userDetails)) {

                // Se crea el permiso de autenticación
                UsernamePasswordAuthenticationToken tokenAutenticacion = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                tokenAutenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Se confirma la autentificacion
                SecurityContextHolder.getContext().setAuthentication(tokenAutenticacion);
            }
        }

        // Continuamos con el resto de los filtros y permitir la petición
        cadenaFiltros.doFilter(request, response);
    }
}
