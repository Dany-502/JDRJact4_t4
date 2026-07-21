package com.springboot.apirest.controller;

import com.springboot.apirest.Dtos.MascotaDTO;
import com.springboot.apirest.entity.Mascota;
import com.springboot.apirest.service.MascotaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService servicioMascota;

    // Endpoint para obtener las mascotas del usuario que inició sesión
    @GetMapping
    public ResponseEntity<Page<Mascota>> verMisMascotas(Authentication autenticacion,
            @PageableDefault(size = 10) Pageable pageable) {
        String correoUsuario = autenticacion.getName();
        Page<Mascota> misMascotas = servicioMascota.obtenerMascotaPorUsuario(correoUsuario, pageable);
        return ResponseEntity.ok(misMascotas);
    }

    // Endpoint para obtener una mascota específica por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Mascota> verMascotaPorId(@PathVariable Integer id, Authentication autenticacion) {
        Mascota mascota = servicioMascota.obtenerMascotaPorId(id);

        if (mascota == null || !mascota.getUsuario().getEmail().equals(autenticacion.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(mascota);
    }

    // Endpoint para registrar una mascota nueva
    @PostMapping
    public ResponseEntity<Mascota> registrarMascota(@Valid @RequestBody MascotaDTO mascotaDto,
            Authentication autenticacion) {
        try {
            String correoUsuario = autenticacion.getName();
            Mascota nuevaMascota = servicioMascota.crearMascota(mascotaDto, correoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMascota);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para modificar una mascota
    @PutMapping("/{id}")
    public ResponseEntity<Mascota> actualizarMascota(@PathVariable Integer id,
            @Valid @RequestBody MascotaDTO mascotaDto, Authentication autenticacion) {
        try {
            String correoUsuario = autenticacion.getName();
            Mascota mascotaActualizada = servicioMascota.actualizarMascota(id, mascotaDto, correoUsuario);
            return ResponseEntity.ok(mascotaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // Endpoint para eliminar una mascota
    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarMascota(@PathVariable Integer id, Authentication autenticacion) {
        try {
            String correoUsuario = autenticacion.getName();
            servicioMascota.borrarMascota(id, correoUsuario);
            return ResponseEntity.ok("Mascota borrada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
