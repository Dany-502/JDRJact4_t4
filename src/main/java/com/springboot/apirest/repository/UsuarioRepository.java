package com.springboot.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.springboot.apirest.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Confirmamos que el usuario existe en la BD a travez de su correo
    boolean existsByEmail(String email);


    // Buscamos un usuario solo por su correo electrónico
    Usuario findByEmail(String email);
}
