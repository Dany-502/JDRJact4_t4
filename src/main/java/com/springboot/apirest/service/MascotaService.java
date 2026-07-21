package com.springboot.apirest.service;

import com.springboot.apirest.Dtos.MascotaDTO;
import com.springboot.apirest.Dtos.UsuarioDTO;
import com.springboot.apirest.entity.Mascota;
import com.springboot.apirest.entity.Usuario;
import com.springboot.apirest.repository.MascotaRepository;
import com.springboot.apirest.repository.UsuarioRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class MascotaService {

    @Autowired
    private MascotaRepository repo;
    @Autowired
    private UsuarioRepository userRepo;

    // Registrar mascota
    public Mascota crearMascota(MascotaDTO mascotadto, String correoUsuario) {
        if (userRepo.existsByEmail(correoUsuario) != true) {
            throw new IllegalArgumentException("El correo electrónico no existe");
        }

        Mascota mascota1 = new Mascota();
        mascota1.setNombre(mascotadto.getNombre());
        mascota1.setEspecie(mascotadto.getEspecie());
        mascota1.setRaza(mascotadto.getRaza());
        mascota1.setColor(mascotadto.getColor());
        mascota1.setGenero(mascotadto.getGenero());
        mascota1.setEdad(mascotadto.getEdad());
        mascota1.setUsuario(userRepo.findByEmail(correoUsuario));
        return repo.save(mascota1);
    }

    // Obtener mascota por usuario con paginación
    public Page<Mascota> obtenerMascotaPorUsuario(String correoUsuario, Pageable pageable) {
        return repo.findByUsuarioEmail(correoUsuario, pageable);
    }

    // Obtener mascota por ID
    public Mascota obtenerMascotaPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    // Actualizar mascota
    public Mascota actualizarMascota(Integer id, MascotaDTO mascotadto, String correoUsuario) {
        Mascota mascota = repo.findById(id).orElse(null);
        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no existe");
        }

        if (!mascota.getUsuario().getEmail().equals(correoUsuario)) {
            throw new IllegalArgumentException("No tienes permiso para actualizar esta mascota");
        }
        mascota.setNombre(mascotadto.getNombre());
        mascota.setEspecie(mascotadto.getEspecie());
        mascota.setRaza(mascotadto.getRaza());
        mascota.setColor(mascotadto.getColor());
        mascota.setGenero(mascotadto.getGenero());
        mascota.setEdad(mascotadto.getEdad());
        return repo.save(mascota);
    }

    public void borrarMascota(Integer id, String correoUsuario) {
        Mascota mascota = repo.findById(id).orElse(null);

        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no existe");
        }
        if (!mascota.getUsuario().getEmail().equals(correoUsuario)) {
            throw new IllegalArgumentException("No tienes permiso para borrar esta mascota");
        }
        repo.deleteById(id);
    }

}
