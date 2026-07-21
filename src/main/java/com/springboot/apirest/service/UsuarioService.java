package com.springboot.apirest.service;

import com.springboot.apirest.Dtos.UsuarioDTO;
import com.springboot.apirest.entity.Usuario;
import com.springboot.apirest.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registrar usuario con el DTO
    public Usuario registrarUsuario(UsuarioDTO usuariodto) {
        if (repo.existsByEmail(usuariodto.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya existe");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(usuariodto.getNombre());
        usuario.setApellido(usuariodto.getApellido());
        usuario.setEmail(usuariodto.getEmail());

        String contraEncriptada = passwordEncoder.encode(usuariodto.getContrasenia());
        usuario.setContrasenia(contraEncriptada);

        return repo.save(usuario);
    }

    // Verificar que el correo y contraseñas ingresados sean validos (Login)
    public boolean verificarCredenciales(String correo, String contraseniaIngresada) {
        Usuario usuario = repo.findByEmail(correo);
        if (usuario == null) {
            return false;
        }
        return passwordEncoder.matches(contraseniaIngresada, usuario.getContrasenia());
    }

    // Cargar usuario por correo
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = repo.findByEmail(correo);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con correo: " + correo);
        }

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getContrasenia(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
